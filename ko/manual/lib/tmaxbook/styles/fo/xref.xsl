<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <xsl:template match="olink" name="olink">
        <xsl:call-template name="anchor"/>
        <xsl:variable name="localinfo" select="@localinfo"/>
        <xsl:choose>
            <xsl:when test="@targetdoc or @targetptr">
                <xsl:variable name="targetdoc.att" select="@targetdoc"/>
                <xsl:variable name="targetptr.att" select="@targetptr"/>
                <xsl:variable name="olink.lang">
                    <xsl:call-template name="l10n.language">
                        <xsl:with-param name="xref-context" select="true()"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="target.database.filename">
                    <xsl:call-template name="select.target.database">
                        <xsl:with-param name="targetdoc.att" select="$targetdoc.att"/>
                        <xsl:with-param name="targetptr.att" select="$targetptr.att"/>
                        <xsl:with-param name="olink.lang" select="$olink.lang"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="target.database"
                              select="document($target.database.filename, /)"/>
                <xsl:if test="$olink.debug != 0">
                    <xsl:message>
                        <xsl:text>Olink debug: root element of target.database is '</xsl:text>
                        <xsl:value-of select="local-name($target.database/*[1])"/>
                        <xsl:text>'.</xsl:text>
                    </xsl:message>
                </xsl:if>
                <xsl:variable name="olink.key">
                    <xsl:call-template name="select.olink.key">
                        <xsl:with-param name="targetdoc.att" select="$targetdoc.att"/>
                        <xsl:with-param name="targetptr.att" select="$targetptr.att"/>
                        <xsl:with-param name="olink.lang" select="$olink.lang"/>
                        <xsl:with-param name="target.database" select="$target.database"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:if test="string-length($olink.key) = 0">
                    <xsl:message>
                        <xsl:text>Error: unresolved olink:</xsl:text>
                        <xsl:text>targetdoc/targetptr = '</xsl:text>
                        <xsl:value-of select="$targetdoc.att"/>
                        <xsl:text>/</xsl:text>
                        <xsl:value-of select="$targetptr.att"/>
                        <xsl:text>'.</xsl:text>
                    </xsl:message>
                </xsl:if>
                <xsl:variable name="href">
                    <xsl:call-template name="make.olink.href">
                        <xsl:with-param name="olink.key" select="$olink.key"/>
                        <xsl:with-param name="target.database" select="$target.database"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="linkend">
                    <xsl:call-template name="olink.as.linkend">
                        <xsl:with-param name="olink.key" select="$olink.key"/>
                        <xsl:with-param name="olink.lang" select="$olink.lang"/>
                        <xsl:with-param name="target.database" select="$target.database"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="hottext">
                    <xsl:call-template name="olink.hottext">
                        <xsl:with-param name="olink.key" select="$olink.key"/>
                        <xsl:with-param name="olink.lang" select="$olink.lang"/>
                        <xsl:with-param name="target.database" select="$target.database"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="olink.docname.citation">
                    <xsl:call-template name="olink.document.citation">
                        <xsl:with-param name="olink.key" select="$olink.key"/>
                        <xsl:with-param name="target.database" select="$target.database"/>
                        <xsl:with-param name="olink.lang" select="$olink.lang"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="olink.page.citation">
                    <xsl:call-template name="olink.page.citation">
                        <xsl:with-param name="olink.key" select="$olink.key"/>
                        <xsl:with-param name="target.database" select="$target.database"/>
                        <xsl:with-param name="olink.lang" select="$olink.lang"/>
                        <xsl:with-param name="linkend" select="$linkend"/>
                    </xsl:call-template>
                </xsl:variable>
                <!-- ../html/xref.xsl 참고 -->
                <xsl:choose>
                    <xsl:when test="$linkend != ''">
                        <fo:basic-link internal-destination="{$linkend}"
                                       xsl:use-attribute-sets="xref.properties">
                            <xsl:copy-of select="$hottext"/>
                            <xsl:copy-of select="$olink.page.citation"/>
                        </fo:basic-link>
                    </xsl:when>
                    <xsl:when test="$href != ''">
                        <xsl:choose>
                            <xsl:when test="$xep.extensions != 0">
                                <xsl:copy-of select="$olink.docname.citation"/>
                                <fo:inline xsl:use-attribute-sets="olink.properties">
                                    <xsl:copy-of select="$hottext"/>
                                </fo:inline>
                                <xsl:copy-of select="$olink.page.citation"/>
                            </xsl:when>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:copy-of select="$olink.docname.citation"/>
                        <xsl:copy-of select="$hottext"/>
                        <xsl:copy-of select="$olink.page.citation"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!-- It looks weird to emphasize referenced titles only for chapters and appendices. -->
    <xsl:template match="chapter|appendix" mode="insert.title.markup">
        <xsl:param name="title"/>
        <xsl:copy-of select="$title"/>
    </xsl:template>

</xsl:stylesheet>