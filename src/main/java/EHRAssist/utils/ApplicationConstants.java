package EHRAssist.utils;


import java.time.LocalDate;

public class ApplicationConstants {
    public static final String DIV =
            "<div xmlns=\"http://www.w3.org/1999/xhtml\">" +
                    "<div class=\"hapiHeaderText\">Barbara <b>GORDON</b></div>" +
                    "<table class=\"hapiPropertyTable\">" +
                    "<tbody>" +
                    "<tr>" +
                    "<td>Date of birth</td>" +
                    "<td><span>17 July 1987</span></td>" +
                    "</tr>" +
                    "</tbody>" +
                    "</table>" +
                    "</div>";

    public static final String CONDITION_URL_1 = "10.131.58.59:481/baseR4/Condition?subject=&code=";
    public static final String CONDITION_URL_2 = "10.131.58.59:481/baseR4?_getpages=&_getpagesoffset=&_count=&_pretty=&_bundletype=";
    public static final String CONDITION_SYSTEM = "http://terminology.hl7.org/CodeSystem/condition-category";

}
