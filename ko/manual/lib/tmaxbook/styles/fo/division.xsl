<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <!-- <part> 표지에서 제목 위, 아래에 여백을 추가한다. -->
    <xsl:template name="division.title">
        <xsl:param name="node" select="."/>
        <xsl:variable name="id">
            <xsl:call-template name="object.id">
                <xsl:with-param name="object" select="$node"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="title">
            <xsl:apply-templates select="$node" mode="object.title.markup"/>
        </xsl:variable>

        <xsl:if test="$passivetex.extensions != 0">
            <fotex:bookmark xmlns:fotex="http://www.tug.org/fotex"
                            fotex-bookmark-level="1"
                            fotex-bookmark-label="{$id}">
                <xsl:value-of select="$title"/>
            </fotex:bookmark>
        </xsl:if>

        <fo:block>
            <xsl:text>&#160;</xsl:text>
        </fo:block>

        <fo:block keep-with-next.within-column="always"
                  hyphenate="false" space-after="10mm">
            <xsl:if test="$axf.extensions != 0">
                <xsl:attribute name="axf:outline-level">
                    <xsl:choose>
                        <xsl:when test="count($node/ancestor::*) > 0">
                            <xsl:value-of select="count($node/ancestor::*)"/>
                        </xsl:when>
                        <xsl:otherwise>1</xsl:otherwise>
                    </xsl:choose>
                </xsl:attribute>
                <xsl:attribute name="axf:outline-expand">false</xsl:attribute>
                <xsl:attribute name="axf:outline-title">
                    <xsl:value-of select="normalize-space($title)"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:copy-of select="$title"/>
        </fo:block>
    </xsl:template>

</xsl:stylesheet>