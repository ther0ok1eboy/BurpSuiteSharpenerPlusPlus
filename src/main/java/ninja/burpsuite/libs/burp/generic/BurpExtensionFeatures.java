// Released under AGPL see LICENSE for more information
// Developed by Soroush Dalili (@irsdl)

package ninja.burpsuite.libs.burp.generic;

public class BurpExtensionFeatures {
    public boolean hasSuiteTab = false;
    public boolean hasContextMenu = false;
    public boolean hasTopMenu = false;
    public boolean hasHttpRequestEditor = false;
    public boolean hasHttpResponseEditor = false;
    public boolean isCommunityVersionCompatible = true;
    public double minSupportedMajorVersionInclusive = 0.0;
    public double maxSupportedMajorVersionInclusive = 0.0;
    public double minSupportedMinorVersionInclusive = 0.0;
    public double maxSupportedMinorVersionInclusive = 0.0;
    public boolean hasHttpHandler = false;
    public boolean hasProxyHandler = false;

    public BurpExtensionFeatures() {

    }
}
