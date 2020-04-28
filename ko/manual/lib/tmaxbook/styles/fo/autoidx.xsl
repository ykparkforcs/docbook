<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [
<!ENTITY % common.entities SYSTEM "../../lib/docbook-xsl/common/entities.ent">
%common.entities;
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <!-- ../html/autoidx.xsl 참고 -->
    
    <xsl:template match="indexterm" mode="index-see">
        <xsl:param name="scope" select="."/>
        <xsl:param name="role" select="''"/>
        <xsl:param name="type" select="''"/>

        <fo:inline>
            <xsl:text>. </xsl:text>
            <fo:inline font-style="italic">
                <xsl:call-template name="gentext">
                    <xsl:with-param name="key" select="'see'"/>
                </xsl:call-template>
            </fo:inline>
            <xsl:text> </xsl:text>
            <xsl:value-of select="see"/>
        </fo:inline>
    </xsl:template>

    <xsl:template match="indexterm" mode="index-seealso">
        <xsl:param name="scope" select="."/>
        <xsl:param name="role" select="''"/>
        <xsl:param name="type" select="''"/>

        <xsl:for-each select="seealso">
            <xsl:sort select="translate(., &lowercase;, &uppercase;)"/>
            <fo:block>
                <fo:inline font-style="italic">
                    <xsl:call-template name="gentext">
                        <xsl:with-param name="key" select="'seealso'"/>
                    </xsl:call-template>
                </fo:inline>
                <xsl:text> </xsl:text>
                <xsl:value-of select="."/>
            </fo:block>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>