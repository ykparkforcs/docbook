<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <xsl:param name="marker.section.level" select="1"/>

    <xsl:param name="body.start.indent">0pc</xsl:param>

    <xsl:param name="default.table.width">158mm</xsl:param>

    <xsl:param name="body.font.master">10</xsl:param>
    <xsl:param name="line-height">1.6</xsl:param>

    <xsl:param name="body.font.family" select="'sans-serif'"/>
    <xsl:param name="dingbat.font.family" select="'sans-serif'"/>

		<xsl:attribute-set name="normal.para.spacing">
        <xsl:attribute name="space-before.optimum">0.5em</xsl:attribute>
        <xsl:attribute name="space-before.minimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-before.maximum">0.6em</xsl:attribute>
        <xsl:attribute name="space-after.minimum">0.9em</xsl:attribute>
        <xsl:attribute name="space-after.optimum">0.8em</xsl:attribute>
        <xsl:attribute name="space-after.maximum">1.0em</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="verbatim.properties">
        <xsl:attribute name="space-before.minimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-before.optimum">0.5em</xsl:attribute>
        <xsl:attribute name="space-before.maximum">0.6em</xsl:attribute>
        <xsl:attribute name="space-after.minimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-after.optimum">0.5em</xsl:attribute>
        <xsl:attribute name="space-after.maximum">0.6em</xsl:attribute>
        <xsl:attribute name="padding-top">0.4em</xsl:attribute>
        <xsl:attribute name="padding-left">0.4em</xsl:attribute>
        <xsl:attribute name="padding-right">0.4em</xsl:attribute>
        <xsl:attribute name="padding-bottom">0.4em</xsl:attribute>
        <xsl:attribute name="margin-left">0em</xsl:attribute>
        <xsl:attribute name="margin-right">0em</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="monospace.properties">
        <xsl:attribute name="font-family">
            <xsl:value-of select="$monospace.font.family"/>
        </xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="monospace.verbatim.properties">
        <xsl:attribute name="font-size">0.9em</xsl:attribute>
    </xsl:attribute-set>

</xsl:stylesheet>