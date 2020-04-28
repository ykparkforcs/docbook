<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

    <xsl:param name="title.font.size" select="36"/>
    <xsl:param name="subtitle.font.size" select="36"/>
    <xsl:param name="titleabbrev.font.size" select="16"/>
    <xsl:param name="acknowledgements.font.size" select="12"/>

    <xsl:template name="book.titlepage.recto">
        <xsl:choose>
            <xsl:when test="bookinfo/title">
                <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/title"/>
            </xsl:when>
            <xsl:when test="info/title">
                <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/title"/>
            </xsl:when>
            <xsl:when test="title or subtitle">
                <p><h1><b><xsl:value-of select="title"/></b><xsl:text>&#160;</xsl:text><xsl:value-of select="subtitle"/></h1></p>
                <p><h3><xsl:value-of select="titleabbrev"/></h3></p><br></br>
            </xsl:when>
        </xsl:choose>

        <!-- 원하는 항목들만 출력 -->
        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/copyright"/>
        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/copyright"/>
        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/legalnotice"/>
        <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="info/legalnotice"/>

        <!-- "Document Info" 부분을 추가로 출력 -->
        <xsl:if test="bookinfo/pubdate or bookinfo/productnumber or bookinfo/edition">
            <p>
                <b>
                <xsl:call-template name="gentext">
                    <xsl:with-param name="key" select="'bookinfo-infotitle'"/>
                </xsl:call-template>
                </b>
            </p>

            <p>
                    <xsl:call-template name="gentext">
                        <xsl:with-param name="key" select="'bookinfo-title'"/>
                    </xsl:call-template>
                    <xsl:text>: </xsl:text>
                    <xsl:value-of select="title"/><xsl:text>&#160;</xsl:text><xsl:value-of select="subtitle"/>
            </p>

            <xsl:if test="bookinfo/pubdate">
                <p>
                    <xsl:call-template name="gentext">
                        <xsl:with-param name="key" select="'bookinfo-pubdate'"/>
                    </xsl:call-template>
                    <xsl:text>: </xsl:text>
                    <xsl:value-of select="bookinfo/pubdate"/>
                </p>
            </xsl:if>

            <xsl:if test="bookinfo/productnumber">
                <p>
                    <xsl:call-template name="gentext">
                        <xsl:with-param name="key" select="'bookinfo-productnumber'"/>
                    </xsl:call-template>
                    <xsl:text>: </xsl:text>
                    <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/productnumber"/>
                </p>
            </xsl:if>

            <xsl:if test="bookinfo/edition">
                <p>
                    <xsl:call-template name="gentext">
                        <xsl:with-param name="key" select="'bookinfo-edition'"/>
                    </xsl:call-template>
                    <xsl:text>: </xsl:text>
                    <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/edition"/>
                </p>
            </xsl:if>

            <xsl:if test="acknowledgements">
                <p>
                    <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="acknowledgements"/>
                </p>
            </xsl:if>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
