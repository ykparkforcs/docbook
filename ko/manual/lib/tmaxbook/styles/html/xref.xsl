<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

    <xsl:template match="chapter|appendix" mode="insert.title.markup">
        <xsl:param name="purpose"/>
        <xsl:param name="xrefstyle"/>
        <xsl:param name="title"/>

        <xsl:choose>
            <!-- <i> 태그로 $title을 감싸지 않도록 수정 -->
            <xsl:when test="$purpose = 'xref'">
                <xsl:copy-of select="$title"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="$title"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

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

                <xsl:variable name="target.database" select="document($target.database.filename,/)"/>

                <xsl:if test="$olink.debug != 0">
                    <xsl:message>
                        <xsl:text>Olink debug: root element of target.database '</xsl:text>
                        <xsl:value-of select="$target.database.filename"/>
                        <xsl:text>' is '</xsl:text>
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

                <xsl:variable name="hottext">
                    <xsl:call-template name="olink.hottext">
                        <xsl:with-param name="target.database" select="$target.database"/>
                        <xsl:with-param name="olink.key" select="$olink.key"/>
                        <xsl:with-param name="olink.lang" select="$olink.lang"/>
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
                    </xsl:call-template>
                </xsl:variable>

                <!--
                 출력 형식 변경
                    * From
                      <a href="...">16장. "jeusadmin"</a>, in JEUS Reference Book
                    * To
                      <a href="...">JEUS Reference Book, 16장. "jeusadmin"</a>

                 gentext_ko.xml에는 다음과 같은 부분이 있다(gentext_ja.xml도 유사하다):
                    <l:context name="xref">
                        <l:template name="olink.document.citation" text="%o, "/>

                 그러나 gentext_ko.xml만으로는 문서 이름과 나머지 부분의 순서까지는 바꿀 수가 없기 때문에 이 템플릿까지 수정해야 했다.

                 TODO: 영어 문서에서는 DocBook XSL 스타일이 적합하다.
                 -->
                <xsl:choose>
                    <xsl:when test="$href != ''">
                        <a href="{$href}" class="olink">
                            <xsl:copy-of select="$olink.docname.citation"/>
                            <xsl:copy-of select="$hottext"/>
                        </a>
                        <xsl:copy-of select="$olink.page.citation"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <span class="olink">
                            <xsl:copy-of select="$olink.docname.citation"/>
                            <xsl:copy-of select="$hottext"/>
                        </span>
                        <xsl:copy-of select="$olink.page.citation"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>

            <xsl:otherwise>
                <xsl:variable name="href">
                    <xsl:choose>
                        <xsl:when test="@linkmode">
                            <xsl:variable name="modespec" select="key('id',@linkmode)"/>
                            <xsl:if test="count($modespec) != 1                           or local-name($modespec) != 'modespec'">
                                <xsl:message>Warning: olink linkmode pointer is wrong.</xsl:message>
                            </xsl:if>
                            <xsl:value-of select="$modespec"/>
                            <xsl:if test="@localinfo">
                                <xsl:text>#</xsl:text>
                                <xsl:value-of select="@localinfo"/>
                            </xsl:if>
                        </xsl:when>
                        <xsl:when test="@type = 'href'">
                            <xsl:call-template name="olink.outline">
                                <xsl:with-param name="outline.base.uri" select="unparsed-entity-uri(@targetdocent)"/>
                                <xsl:with-param name="localinfo" select="@localinfo"/>
                                <xsl:with-param name="return" select="'href'"/>
                            </xsl:call-template>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$olink.resolver"/>
                            <xsl:text>?</xsl:text>
                            <xsl:value-of select="$olink.sysid"/>
                            <xsl:value-of select="unparsed-entity-uri(@targetdocent)"/>
                            <xsl:if test="@localinfo">
                                <xsl:text>&amp;</xsl:text>
                                <xsl:value-of select="$olink.fragid"/>
                                <xsl:value-of select="@localinfo"/>
                            </xsl:if>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>

                <xsl:choose>
                    <xsl:when test="$href != ''">
                        <a href="{$href}" class="olink">
                            <xsl:call-template name="olink.hottext"/>
                        </a>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="olink.hottext"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
