<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <xsl:template match="footnote">
        <xsl:choose>
            <xsl:when test="ancestor::table or ancestor::informaltable">
                <xsl:call-template name="format.footnote.mark">
                    <xsl:with-param name="mark">
                        <xsl:apply-templates select="." mode="footnote.number"/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <fo:footnote>
                    <fo:inline>
                        <xsl:call-template name="format.footnote.mark">
                            <xsl:with-param name="mark">
                                <xsl:apply-templates select="." mode="footnote.number"/>
                            </xsl:with-param>
                        </xsl:call-template>
                    </fo:inline>
                    <fo:footnote-body xsl:use-attribute-sets="footnote.properties">
                        <!--
                         DocBook XSL에는 <footnote>의 내용만 출력하는 문제가 있어서
                         footnote.number 값을 <footnote> 앞에 붙이도록 수정했다.
                         -->
                        <fo:list-block>
                            <fo:list-item xsl:use-attribute-sets="list.item.spacing">
                                <fo:list-item-label>
                                    <fo:block>
                                        <xsl:call-template name="format.footnote.mark">
                                            <xsl:with-param name="mark">
                                                <xsl:apply-templates select="." mode="footnote.number"/>
                                            </xsl:with-param>
                                        </xsl:call-template>
                                    </fo:block>
                                </fo:list-item-label>
                                <fo:list-item-body start-indent="1.2em">
                                    <xsl:apply-templates/>
                                </fo:list-item-body>
                            </fo:list-item>
                        </fo:list-block>
                    </fo:footnote-body>
                </fo:footnote>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>