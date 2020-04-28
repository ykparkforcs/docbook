<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <xsl:template match="processing-instruction('hard-pagebreak')">
        <fo:block break-before='page'/>
    </xsl:template>

    <!-- 현재 element에 keep-together(dbfo) processing instruction이 있으면 그 값을, 없으면 기본값 auto를 리턴한다. -->
    <xsl:template name="dbfo-keep-together">
        <xsl:param name="default">auto</xsl:param>

        <xsl:variable name="pi-keep-together">
            <xsl:call-template name="dbfo-attribute">
                <xsl:with-param name="pis" select="processing-instruction('dbfo')"/>
                <xsl:with-param name="attribute" select="'keep-together'"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:choose>
            <xsl:when test="$pi-keep-together = ''">
                <xsl:value-of select="$default"/>
            </xsl:when>
            <xsl:otherwise><xsl:value-of select="$pi-keep-together"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>