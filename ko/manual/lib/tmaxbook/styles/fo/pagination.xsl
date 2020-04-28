<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <xsl:param name="paper.type" select="'A4'"/>
    <xsl:param name="double.sided" select="'1'"/>

    <xsl:param name="page.margin.inner">0.80in</xsl:param>
    <xsl:param name="page.margin.outer">0.60in</xsl:param>
    <xsl:param name="page.margin.top" select="'15mm'"/>
    <xsl:param name="region.before.extent" select="'6mm'"/>
    <xsl:param name="header.table.height" select="'5.8mm'"/>
    <xsl:param name="body.margin.top" select="'12mm'"/>
    <xsl:param name="header.rule" select="0"/>

    <xsl:param name="page.margin.bottom" select="'15mm'"/>
    <xsl:param name="region.after.extent" select="'6mm'"/>
    <xsl:param name="footer.table.height" select="'5.8mm'"/>
    <xsl:param name="body.margin.bottom" select="'12mm'"/>
    <xsl:param name="body.start.indent" select="'13mm'"/>
    <xsl:param name="footer.rule" select="1"/>

    <xsl:param name="header.column.widths">0 1 0</xsl:param>
    <xsl:param name="footer.column.widths">1 0 1</xsl:param>

    <xsl:attribute-set name="header.content.properties">
        <xsl:attribute name="font-size">0.95em</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="color">#555555</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="footer.content.properties">
        <xsl:attribute name="font-size">0.95em</xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
    </xsl:attribute-set>

</xsl:stylesheet>