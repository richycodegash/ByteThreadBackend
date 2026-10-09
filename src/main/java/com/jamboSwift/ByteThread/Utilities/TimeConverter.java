package com.jamboSwift.ByteThread.Utilities;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * Converts UTC {@link Instant}s (as stored in entities) to a client's local time.
 *
 * Usage:
 *  - On a desktop/mobile client (e.g. JavaFX): use the overloads that take no zone;
 *    they use the machine's own time zone via {@link ZoneId#systemDefault()}.
 *  - On the server: take the client's IANA zone id (e.g. "Africa/Nairobi",
 *    "America/New_York") from a header or request param and pass it in.
 *
 * Always use IANA region ids, not fixed offsets like "+03:00", so that
 * daylight-saving rules are applied correctly.
 */
public final class TimeConverter {

    private TimeConverter() {}

    // ---------- Zone resolution ----------

    /** Parses a zone id safely; falls back to UTC if null, blank or invalid. */
    public static ZoneId resolveZone(String zoneId) {
        if (zoneId == null || zoneId.isBlank()) {
            return ZoneOffset.UTC;
        }
        try {
            return ZoneId.of(zoneId.trim());
        } catch (DateTimeException e) {
            return ZoneOffset.UTC;
        }
    }

    // ---------- Core conversions ----------

    /** Instant -> ZonedDateTime in the given zone. */
    public static ZonedDateTime toZoned(Instant instant, ZoneId zone) {
        Objects.requireNonNull(instant, "instant");
        Objects.requireNonNull(zone, "zone");
        return instant.atZone(zone);
    }

    public static ZonedDateTime toZoned(Instant instant, String zoneId) {
        return toZoned(instant, resolveZone(zoneId));
    }

    /** Instant -> ZonedDateTime in the machine's own zone (client-side use). */
    public static ZonedDateTime toLocal(Instant instant) {
        return toZoned(instant, ZoneId.systemDefault());
    }

    /** Instant -> LocalDateTime in the given zone (drops zone info; display only). */
    public static LocalDateTime toLocalDateTime(Instant instant, String zoneId) {
        return toZoned(instant, zoneId).toLocalDateTime();
    }

    /** The reverse direction: a user-entered local date-time -> Instant to store. */
    public static Instant toInstant(LocalDateTime local, String zoneId) {
        return local.atZone(resolveZone(zoneId)).toInstant();
    }

    // ---------- Formatting ----------

    /** Locale-aware formatting, e.g. "Oct 7, 2026, 3:00 PM" depending on locale. */
    public static String format(Instant instant, String zoneId, Locale locale,
                                FormatStyle dateStyle, FormatStyle timeStyle) {
        DateTimeFormatter f = DateTimeFormatter
                .ofLocalizedDateTime(dateStyle, timeStyle)
                .withLocale(locale == null ? Locale.getDefault() : locale);
        return f.format(toZoned(instant, zoneId));
    }

    /** Custom pattern, e.g. "dd MMM yyyy HH:mm". */
    public static String format(Instant instant, String zoneId, String pattern) {
        return DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                .format(toZoned(instant, zoneId));
    }

    /** Same as above but using the machine's own zone (client-side use). */
    public static String formatLocal(Instant instant, String pattern) {
        return DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
                .format(toLocal(instant));
    }

    // ---------- Chat-style display ----------

    /**
     * Chat-friendly timestamp:
     *  - same day      -> "14:32"
     *  - yesterday     -> "Yesterday 14:32"
     *  - same year     -> "7 Oct 14:32"
     *  - older         -> "7 Oct 2025 14:32"
     */
    public static String chatTimestamp(Instant instant, String zoneId) {
        ZoneId zone = resolveZone(zoneId);
        ZonedDateTime time = toZoned(instant, zone);
        LocalDate today = LocalDate.now(zone);
        LocalDate day = time.toLocalDate();

        String hm = DateTimeFormatter.ofPattern("HH:mm").format(time);
        if (day.equals(today)) return hm;
        if (day.equals(today.minusDays(1))) return "Yesterday " + hm;
        if (day.getYear() == today.getYear()) {
            return DateTimeFormatter.ofPattern("d MMM", Locale.getDefault()).format(time) + " " + hm;
        }
        return DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault()).format(time) + " " + hm;
    }

    /** "just now", "5 min ago", "3 h ago", "2 d ago" (falls back to a date after a week). */
    public static String timeAgo(Instant instant, String zoneId) {
        Duration d = Duration.between(instant, Instant.now());
        if (d.isNegative()) return "just now"; // small clock skew between devices
        long s = d.getSeconds();
        if (s < 60) return "just now";
        if (s < 3600) return (s / 60) + " min ago";
        if (s < 86_400) return (s / 3600) + " h ago";
        if (s < 7 * 86_400) return (s / 86_400) + " d ago";
        return format(instant, zoneId, "d MMM yyyy");
    }

    public static String toIsoLocal(Instant instant, String zoneId) {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(toZoned(instant, zoneId));
    }
}
