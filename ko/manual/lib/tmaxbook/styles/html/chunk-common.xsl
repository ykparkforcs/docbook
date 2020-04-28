<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

    <xsl:template name="header.navigation">
        <xsl:param name="prev" select="/foo"/>
        <xsl:param name="next" select="/foo"/>
        <xsl:param name="nav.context"/>

        <xsl:variable name="home" select="/*[1]"/>
        <xsl:variable name="up" select="parent::*"/>

        <xsl:variable name="row1" select="$navig.showtitles != 0"/>
        <xsl:variable name="row2"
                      select="  count($prev) &gt; 0 or
                                (   count($up) &gt; 0 and
                                    generate-id($up) != generate-id($home) and
                                    $navig.showtitles != 0
                                ) or
                                count($next) &gt; 0"/>

        <!--
         <th> 태그에 배경색을 주도록 CSS를 수정하자 이 navigation header에 사용한 <th>에도 배경색이 들어가는 문제가 있어서
         이 부분에서 <th> 대신 <td class="navbar-title" ...>를 사용하도록 수정하였다.
         CSS에서는 navbar-title 클래스에 굵게 효과를 주도록 한다.
         -->
        <xsl:if test="$suppress.navigation = '0' and $suppress.header.navigation = '0'">
            <div class="navheader">
                <xsl:if test="$row1 or $row2">
                    <table width="100%" summary="Navigation header">
                        <xsl:if test="$row1">
                            <tr>
                                <td class="navbar-title" colspan="3" align="center">
                                    <xsl:apply-templates select="." mode="object.title.markup"/>
                                </td>
                            </tr>
                        </xsl:if>

                        <xsl:if test="$row2">
                            <tr>
                                <td width="20%" align="left">
                                    <xsl:if test="count($prev)&gt;0">
                                        <a accesskey="p">
                                            <xsl:attribute name="href">
                                                <xsl:call-template name="href.target">
                                                    <xsl:with-param name="object" select="$prev"/>
                                                </xsl:call-template>
                                            </xsl:attribute>
                                            <xsl:call-template name="navig.content">
                                                <xsl:with-param name="direction" select="'prev'"/>
                                            </xsl:call-template>
                                        </a>
                                    </xsl:if>
                                    <xsl:text>&#160;</xsl:text>
                                </td>

                                <td class="navbar-title" width="60%" align="center">
                                    <xsl:choose>
                                        <xsl:when test="    count($up) &gt; 0 and
                                                            generate-id($up) != generate-id($home) and
                                                            $navig.showtitles != 0">
                                            <xsl:apply-templates select="$up" mode="object.title.markup"/>
                                        </xsl:when>
                                        <xsl:otherwise>&#160;</xsl:otherwise>
                                    </xsl:choose>
                                </td>

                                <td width="20%" align="right">
                                    <xsl:text>&#160;</xsl:text>
                                    <xsl:if test="count($next)&gt;0">
                                        <a accesskey="n">
                                            <xsl:attribute name="href">
                                                <xsl:call-template name="href.target">
                                                    <xsl:with-param name="object" select="$next"/>
                                                </xsl:call-template>
                                            </xsl:attribute>
                                            <xsl:call-template name="navig.content">
                                                <xsl:with-param name="direction" select="'next'"/>
                                            </xsl:call-template>
                                        </a>
                                    </xsl:if>
                                </td>
                            </tr>
                        </xsl:if>
                    </table>
                </xsl:if>
                
                <xsl:if test="$header.rule != 0">
                    <hr/>
                </xsl:if>
            </div>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
