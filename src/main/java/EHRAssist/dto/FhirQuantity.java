package EHRAssist.dto;

public class FhirQuantity {

    private String prefix;   // gt, lt, ge, le, eq, ne or ""
    private Double number;   // nullable
    private String unit;     // never null

    public FhirQuantity(String prefix, Double number, String unit) {
        this.prefix = prefix != null ? prefix : "";
        this.number = number;               // may be null
        this.unit = unit != null ? unit : "";
    }

    public String getPrefix() {
        return prefix;
    }

    public Double getNumber() {
        return number;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "FhirQuantity{" +
                "prefix='" + prefix + '\'' +
                ", number=" + number +
                ", unit='" + unit + '\'' +
                '}';
    }

    // ========= SAFE PARSER =========
    public static FhirQuantity parse(String valueQuantity) {

        // No filter provided → only number is null
        if (valueQuantity == null || valueQuantity.isBlank()) {
            return new FhirQuantity("", null, "");
        }

        if (!valueQuantity.contains("|")) {
            throw new IllegalArgumentException("Invalid value-quantity format: " + valueQuantity);
        }

        String[] parts = valueQuantity.split("\\|");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid value-quantity format: " + valueQuantity);
        }

        String numberPart = parts[0].trim();
        String unit = parts[1].trim();   // never null

        String prefix = "";
        String numericStr = numberPart;
        String[] prefixes = {"eq", "ne", "gt", "ge", "lt", "le"};

        for (String p : prefixes) {
            if (numberPart.startsWith(p)) {
                prefix = p;
                numericStr = numberPart.substring(p.length());
                break;
            }
        }

        Double number;
        try {
            number = Double.parseDouble(numericStr);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Invalid numeric value in value-quantity: " + numericStr, ex);
        }

        return new FhirQuantity(prefix, number, unit);
    }
}
