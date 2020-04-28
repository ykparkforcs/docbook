<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <!--
     ulink.hyphenate, ulink.hyphenate.chars는 DocBook XSL에 정의되어 있는 param들이다.

     나머지 코드는 특정 element들에 대해 ulink.hyphenate와 비슷한 효과를 내기 위해 추가한 customizing이다.
     Java 이외의 프로그래밍 언어를 처리할 때는 다른 customizing이 필요할 수 있다.
     -->

    <!-- link@xlink:href -->
    <xsl:param name="ulink.hyphenate" select="'&#x200b;'"/>
    <xsl:param name="ulink.hyphenate.chars">:/@&amp;?.#</xsl:param>

    <!-- classname, interfacename, methodname, packagename -->
    <xsl:param name="ooname.hyphenate" select="'&#x200b;'"/>
    <xsl:param name="ooname.hyphenate.chars">.</xsl:param>

    <!-- classname, interfacename, methodname -->
    <xsl:param name="ooname.camelhumps.hyphenate" select="'&#x200b;'"/>

    <!-- filename -->
    <xsl:param name="filename.hyphenate" select="'&#x200b;'"/>
    <xsl:param name="filename.hyphenate.chars">/\.-_</xsl:param>

    <!-- property -->
    <xsl:param name="property.hyphenate" select="'&#x200b;'"/>
    <xsl:param name="property.hyphenate.chars">.</xsl:param>

    <!-- property -->
    <xsl:param name="property.camelhumps.hyphenate" select="'&#x200b;'"/>

    <xsl:template match="classname | interfacename | methodname">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <xsl:variable name="hyphenated.content">
            <xsl:call-template name="hyphenate-content">
                <xsl:with-param name="content" select="$content"/>
                <xsl:with-param name="hyphen" select="$ooname.hyphenate"/>
                <xsl:with-param name="candidates" select="$ooname.hyphenate.chars"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="camelhumps.hyphenated.content">
            <xsl:call-template name="camelhumps-hyphenate-content">
                <xsl:with-param name="content" select="$hyphenated.content"/>
                <xsl:with-param name="last.case" select="''"/>
                <xsl:with-param name="camelhumps.hyphenate" select="$ooname.camelhumps.hyphenate"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="inline.monoseq">
            <xsl:with-param name="content" select="$camelhumps.hyphenated.content"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="package">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <xsl:variable name="hyphenated.content">
            <xsl:call-template name="hyphenate-content">
                <xsl:with-param name="content" select="$content"/>
                <xsl:with-param name="hyphen" select="$ooname.hyphenate"/>
                <xsl:with-param name="candidates" select="$ooname.hyphenate.chars"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="inline.charseq">
            <xsl:with-param name="content" select="$hyphenated.content"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="filename">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <xsl:variable name="hyphenated.content">
            <xsl:call-template name="hyphenate-content">
                <xsl:with-param name="content" select="$content"/>
                <xsl:with-param name="hyphen" select="$filename.hyphenate"/>
                <xsl:with-param name="candidates" select="$filename.hyphenate.chars"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="inline.monoseq">
            <xsl:with-param name="content" select="$hyphenated.content"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="property">
        <xsl:variable name="content">
            <xsl:apply-templates/>
        </xsl:variable>
        <xsl:variable name="hyphenated.content">
            <xsl:call-template name="hyphenate-content">
                <xsl:with-param name="content" select="$content"/>
                <xsl:with-param name="hyphen" select="$property.hyphenate"/>
                <xsl:with-param name="candidates" select="$property.hyphenate.chars"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="camelhumps.hyphenated.content">
            <xsl:call-template name="camelhumps-hyphenate-content">
                <xsl:with-param name="content" select="$hyphenated.content"/>
                <xsl:with-param name="last.case" select="''"/>
                <xsl:with-param name="camelhumps.hyphenate" select="$property.camelhumps.hyphenate"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="inline.monoseq">
            <xsl:with-param name="content" select="$camelhumps.hyphenated.content"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="hyphenate-content">
        <xsl:param name="content" select="''"/>
        <xsl:param name="hyphen" select="''"/>
        <xsl:param name="candidates" select="''"/>
        <xsl:choose>
            <xsl:when test="$hyphen = '' or $candidates = '' or string-length($content) &lt;= 1">
                <xsl:value-of select="$content"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="char" select="substring($content, 1, 1)"/>
                <xsl:value-of select="$char"/>
                <xsl:if test="contains($candidates, $char)">
                    <xsl:copy-of select="$hyphen"/>
                </xsl:if>
                <xsl:call-template name="hyphenate-content">
                    <xsl:with-param name="content" select="substring($content, 2)"/>
                    <xsl:with-param name="hyphen" select="$hyphen"/>
                    <xsl:with-param name="candidates" select="$candidates"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="camelhumps-hyphenate-content">
        <xsl:param name="content" select="''"/>
        <xsl:param name="last.case" select="''"/>
        <xsl:param name="camelhumps.hyphenate" select="''"/>
        <xsl:choose>
            <xsl:when test="$camelhumps.hyphenate = '' or string-length($content) &lt;= 1">
                <xsl:value-of select="$content"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="char" select="substring($content, 1, 1)"/>
                <xsl:variable name="current.case">
                    <xsl:choose>
                        <xsl:when test="contains('ABCDEFGHIJKLMNOPQRSTUVWXYZ', $char)">upper</xsl:when>
                        <xsl:when test="contains('abcdefghijklmnopqrstuvwxyz', $char)">lower</xsl:when>
                        <xsl:otherwise>other</xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:if test="$last.case = 'lower' and $current.case = 'upper'">
                    <xsl:value-of select="$camelhumps.hyphenate"/>
                </xsl:if>
                <xsl:value-of select="$char"/>
                <xsl:call-template name="camelhumps-hyphenate-content">
                    <xsl:with-param name="content" select="substring($content, 2)"/>
                    <xsl:with-param name="last.case" select="$current.case"/>
                    <xsl:with-param name="camelhumps.hyphenate" select="$camelhumps.hyphenate"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>