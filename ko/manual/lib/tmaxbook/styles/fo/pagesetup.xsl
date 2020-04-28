<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <xsl:template name="header.content">
        <xsl:param name="pageclass" select="''"/>
        <xsl:param name="sequence" select="''"/>
        <xsl:param name="position" select="''"/>
        <xsl:param name="gentext-key" select="''"/>
        <!--
        <xsl:choose>
            <xsl:when test="$pageclass = 'titlepage' and $gentext-key = 'book'"/>
            <xsl:when test="$sequence = 'odd' and $position = 'center'">
                <fo:block text-align="right">
                    <xsl:apply-templates select="." mode="object.title.markup"/>
                </fo:block>
            </xsl:when>
            <xsl:when test="$sequence = 'even' and $position = 'center'">
                <fo:block text-align="left">
                    <xsl:apply-templates select="." mode="object.title.markup"/>
                </fo:block>
            </xsl:when>
        </xsl:choose>
        -->
    </xsl:template>

    <xsl:template name="footer.content">
        <xsl:param name="pageclass" select="''"/>
        <xsl:param name="sequence" select="''"/>
        <xsl:param name="position" select="''"/>
        <xsl:param name="gentext-key" select="''"/>
        <xsl:choose>
            <xsl:when test="$pageclass = 'titlepage' and $gentext-key = 'book'"/>
            <xsl:when test="$sequence = 'even' and $position = 'left'">
                <fo:block text-align="left">
                  <fo:page-number/>
                  <xsl:text>&#xA0;&#xA0;&#xA0;</xsl:text>
                  <xsl:value-of select="ancestor-or-self::book/title"/>
                  <xsl:text> </xsl:text>
                  <xsl:value-of select="ancestor-or-self::book/subtitle"/>
                </fo:block>
            </xsl:when>
            <xsl:when test="($sequence = 'odd' or $sequence = 'first') and $position = 'right'">
                <fo:block text-align="right">
                  <xsl:apply-templates select="." mode="object.title.markup"/>
                  <xsl:text>&#xA0;&#xA0;&#xA0;</xsl:text>
                   <fo:page-number/>
                </fo:block>
            </xsl:when>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>