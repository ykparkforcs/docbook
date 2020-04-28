<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <!--###################################################
        ## fo/lists.xsl
        ################################################### -->

    <!-- for <?dbfo keep-together?> -->
    <xsl:template match="itemizedlist/listitem">
        <xsl:variable name="id">
            <xsl:call-template name="object.id"/>
        </xsl:variable>

        <!-- keep-together 변수 선언 -->
        <xsl:variable name="keep-together">
            <xsl:call-template name="dbfo-keep-together"/>
        </xsl:variable>

        <xsl:variable name="item.contents">
            <fo:list-item-label end-indent="label-end()" xsl:use-attribute-sets="itemizedlist.label.properties">
                <fo:block>
                    <xsl:call-template name="itemizedlist.label.markup">
                        <xsl:with-param name="itemsymbol">
                            <xsl:call-template name="list.itemsymbol">
                                <xsl:with-param name="node" select="parent::itemizedlist"/>
                            </xsl:call-template>
                        </xsl:with-param>
                    </xsl:call-template>
                </fo:block>
            </fo:list-item-label>
            <fo:list-item-body start-indent="body-start()">
                <xsl:choose>
                    <xsl:when test="$passivetex.extensions = '1'">
                        <xsl:apply-templates/>
                    </xsl:when>
                    <xsl:otherwise>
                        <!-- keep-together.within-column 속성 값 설정 -->
                        <fo:block keep-together.within-column="{$keep-together}">
                            <xsl:apply-templates/>
                        </fo:block>
                    </xsl:otherwise>
                </xsl:choose>
            </fo:list-item-body>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="parent::*/@spacing = 'compact'">
                <fo:list-item id="{$id}" xsl:use-attribute-sets="compact.list.item.spacing">
                    <xsl:copy-of select="$item.contents"/>
                </fo:list-item>
            </xsl:when>
            <xsl:otherwise>
                <fo:list-item id="{$id}" xsl:use-attribute-sets="list.item.spacing">
                    <xsl:copy-of select="$item.contents"/>
                </fo:list-item>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--
     HTML 출력물과 비슷한 기호를 사용하고, 'none'인 경우 기호 없이 사용할 수 있게 했다.
     -->
    <xsl:template name="itemizedlist.label.markup">
        <xsl:param name="itemsymbol" select="'disc'"/>
        <fo:inline font-family="bullet">
            <xsl:choose>
                <!-- bullet -->
                <xsl:when test="$itemsymbol='disc'">&#x25CF;</xsl:when>
                <!-- whitebullet -->
                <xsl:when test="$itemsymbol='circle'">&#x2013;</xsl:when>
                <!-- smallblacksquare -->
                <xsl:when test="$itemsymbol='square'">&#x2022;</xsl:when>
                <xsl:when test="$itemsymbol='none'"/>
                <!-- bullet -->
                <xsl:otherwise>&#x2022;</xsl:otherwise>
            </xsl:choose>
        </fo:inline>
    </xsl:template>

    <!-- for <?dbfo keep-together?> -->
    <xsl:template match="orderedlist/listitem">
        <xsl:variable name="id">
            <xsl:call-template name="object.id"/>
        </xsl:variable>

        <!-- keep-together 변수 선언 -->
        <xsl:variable name="keep-together">
            <xsl:call-template name="dbfo-keep-together"/>
        </xsl:variable>

        <xsl:variable name="item.contents">
            <fo:list-item-label end-indent="label-end()" xsl:use-attribute-sets="orderedlist.label.properties">
                <fo:block>
                    <xsl:apply-templates select="." mode="item-number"/>
                </fo:block>
            </fo:list-item-label>
            <fo:list-item-body start-indent="body-start()">
                <!-- keep-together.within-column 속성 값 설정 -->
                <fo:block keep-together.within-column="{$keep-together}">
                    <xsl:apply-templates/>
                </fo:block>
            </fo:list-item-body>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="parent::*/@spacing = 'compact'">
                <fo:list-item id="{$id}" xsl:use-attribute-sets="compact.list.item.spacing">
                    <xsl:copy-of select="$item.contents"/>
                </fo:list-item>
            </xsl:when>
            <xsl:otherwise>
                <fo:list-item id="{$id}" xsl:use-attribute-sets="list.item.spacing">
                    <xsl:copy-of select="$item.contents"/>
                </fo:list-item>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>