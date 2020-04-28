<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <xsl:output method="xml" encoding="utf-8"/>

    <xsl:param name="xep.extensions">1</xsl:param>
    <xsl:param name="ulink.show" select="0"/>
    <xsl:param name="email.delimiters.enabled">0</xsl:param>
    <xsl:param name="callout.graphics">0</xsl:param>
    <xsl:param name="callout.unicode">1</xsl:param>
    <xsl:param name="variablelist.as.blocks">1</xsl:param>
    <xsl:param name="glossary.as.blocks">1</xsl:param>

    <xsl:param name="generate.toc">
        book    toc,title,figure,table,example,equation
    </xsl:param>

    <xsl:param name="autotoc.label.separator" select="'&#160;&#160;&#160;&#160;'"/>

    <xsl:attribute-set name="xref.properties">
        <xsl:attribute name="color">#0066CC</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="olink.properties">
        <xsl:attribute name="color">#000000</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="list.block.spacing" use-attribute-sets="normal.para.spacing">
        <xsl:attribute name="space-after.optimum">0.5em</xsl:attribute>
        <xsl:attribute name="space-after.minimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-after.maximum">0.6em</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="list.item.spacing" use-attribute-sets="normal.para.spacing"/>

    <xsl:attribute-set name="toc.line.properties">
        <xsl:attribute name="font-weight">
            <xsl:choose>
                <xsl:when test="self::part | self::chapter | self::preface | self::appendix |
                                self::glossary | self::bibliography | self::index">bold</xsl:when>
                <xsl:otherwise>normal</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
        <xsl:attribute name="space-before">
            <xsl:choose>
                <xsl:when test="self::part | self::chapter | self::preface | self::appendix |
                                self::glossary | self::bibliography | self::index">0.5em</xsl:when>
                <xsl:when test="self::sect1 or self::figure or self::example or self::table">0em</xsl:when>
                <xsl:when test="self::sect2">0.1em</xsl:when>
                <xsl:otherwise>0em</xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:attribute-set>

    <!-- note, tip, caution, warning title porperties -->
    <xsl:attribute-set name="admonition.title.properties">
        <xsl:attribute name="start-indent">4.2em</xsl:attribute>
        <xsl:attribute name="font-size">10pt</xsl:attribute>
        <xsl:attribute name="color">#555555</xsl:attribute>
        <xsl:attribute name="padding-top">2mm</xsl:attribute>
        <xsl:attribute name="padding-right">1mm</xsl:attribute>
        <xsl:attribute name="padding-left">1mm</xsl:attribute>
        <xsl:attribute name="border-top">1pt solid #000000</xsl:attribute>
    </xsl:attribute-set>

    <!-- note, tip, caution, warning porperties -->
    <xsl:attribute-set name="admonition.properties">
        <xsl:attribute name="start-indent">4.2em</xsl:attribute>
        <xsl:attribute name="padding-right">1mm</xsl:attribute>
        <xsl:attribute name="padding-left">1mm</xsl:attribute>
        <xsl:attribute name="border-bottom">1pt solid #000000</xsl:attribute>
    </xsl:attribute-set>
    
    <!-- font size control -->
    <xsl:attribute-set name="monospace.verbatim.properties" use-attribute-sets="verbatim.properties monospace.properties">
      <xsl:attribute name="font-size">
      <xsl:choose>
      <xsl:when test="processing-instruction('db-font-size')"><xsl:value-of
           select="processing-instruction('db-font-size')"/></xsl:when>
      <xsl:otherwise>inherit</xsl:otherwise>
      </xsl:choose>
      </xsl:attribute>
    </xsl:attribute-set>
    
    <!-- xml profiling option -->
    <xsl:param name="profile.condition" select="$target.product"/>

    <!-- 영문일 경우, 단어단위로 잘리지 않게, 왼쪽 정렬로 설정 -->
    <xsl:param name="hyphenate">
        <xsl:if test="$l10n.gentext.default.language != 'en'">true</xsl:if>
        <xsl:if test="$l10n.gentext.default.language = 'en'">false</xsl:if>
    </xsl:param>
    <xsl:param name="alignment">
        <xsl:if test="$l10n.gentext.default.language != 'en'">justify</xsl:if>
        <xsl:if test="$l10n.gentext.default.language = 'en'">left</xsl:if>
    </xsl:param>

</xsl:stylesheet>
