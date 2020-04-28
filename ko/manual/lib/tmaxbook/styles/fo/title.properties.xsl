<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <xsl:attribute-set name="component.title.properties">
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 2.4"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="text-align">
          <xsl:choose>
              <xsl:when test="parent::chapter | parent::appendix">right</xsl:when>
              <xsl:when test="    (
                                    (parent::article | parent::articleinfo | parent::info/parent::article) and
                                    not(ancestor::book) and
                                    not(self::bibliography)
                                ) or (
                                    parent::slides | parent::slidesinfo
                                )">center</xsl:when>
              <xsl:when test="self::preface">right</xsl:when>
              <!-- glossary alignment (right)
              <xsl:when test="self::preface | self::glossary | self::index">right</xsl:when>
              -->
              <!-- index item alignment
              <xsl:when test="ancestor::book/index">right</xsl:when>
              -->
            <xsl:otherwise>left</xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:attribute name="space-after">20mm</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="section.title.level1.properties">
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 1.8"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="space-before.minimum">0.9em</xsl:attribute>
        <xsl:attribute name="space-before.optimum">1.1em</xsl:attribute>
        <xsl:attribute name="space-before.maximum">1.3em</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="section.title.level2.properties">
        <xsl:attribute name="space-before">10mm</xsl:attribute>
        <xsl:attribute name="color">#555555</xsl:attribute>
        <xsl:attribute name="font-family">
            <xsl:value-of select="$body.font.family"/>
        </xsl:attribute>
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 1.6"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="section.title.level3.properties">
        <xsl:attribute name="color">#555555</xsl:attribute>
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 1.4"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <!-- Level3: Italic 제거
        <xsl:attribute name="font-style">italic</xsl:attribute>
         -->
    </xsl:attribute-set>

    <xsl:attribute-set name="section.title.level4.properties">
        <xsl:attribute name="color">#555555</xsl:attribute>
        <xsl:attribute name="font-family">
            <xsl:value-of select="$body.font.family"/>
        </xsl:attribute>
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 1.2"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="section.title.level5.properties">
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master * 1.0"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="font-style">italic</xsl:attribute>
    </xsl:attribute-set>

    <xsl:attribute-set name="formal.title.properties" use-attribute-sets="normal.para.spacing">
        <xsl:attribute name="font-size">
            <xsl:value-of select="$body.font.master"/>
            <xsl:text>pt</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="space-after.minimum">0.4em</xsl:attribute>
        <xsl:attribute name="space-after.optimum">0.5em</xsl:attribute>
        <xsl:attribute name="space-after.maximum">0.6em</xsl:attribute>
    </xsl:attribute-set>

</xsl:stylesheet>