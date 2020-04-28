<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:exsl="http://exslt.org/common"
                version='1.0'>

    <xsl:template match="programlisting|screen">
        <xsl:param name="suppress-numbers" select="'0'"/>
        <xsl:variable name="id">
            <xsl:call-template name="object.id"/>
        </xsl:variable>

        <xsl:variable name="content">
            <xsl:choose>
                <xsl:when test="$suppress-numbers = '0'
                          and @linenumbering = 'numbered'
                          and $use.extensions != '0'
                          and $linenumbering.extension != '0'">
                    <xsl:call-template name="number.rtf.lines">
                        <xsl:with-param name="rtf">
                            <xsl:call-template name="apply-highlighting"/>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="apply-highlighting"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <!--
         DocBook XSL은 shade.verbatim 값이 0이면 <programlisting>, <screen>, <synopsis>에 모두 배경색을 넣지 않고,
         그 외의 값이면 모두 배경색을 넣는다.

         여기에서는 shade.verbatim 값에 관계 없이 <programlisting>과 <screen>에는 배경색을 넣도록 설정한다.
         -->
        <fo:block id="{$id}"
                  xsl:use-attribute-sets="monospace.verbatim.properties shade.verbatim.style">
            <xsl:choose>
                <xsl:when test="$hyphenate.verbatim != 0 and function-available('exsl:node-set')">
                    <xsl:apply-templates select="exsl:node-set($content)" mode="hyphenate.verbatim"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:copy-of select="$content"/>
                </xsl:otherwise>
            </xsl:choose>
        </fo:block>
    </xsl:template>

</xsl:stylesheet>