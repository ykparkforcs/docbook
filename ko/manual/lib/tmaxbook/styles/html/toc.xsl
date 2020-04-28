<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.w3.org/1999/xhtml"
                version="1.0">

    <xsl:output method="xml" encoding="UTF-8" indent="no"
                doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
                doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>

    <xsl:template match="html">
        <html>
            <xsl:apply-templates/>
        </html>
    </xsl:template>

    <xsl:template match="head">
        <head>
            <meta xmlns="" http-equiv="Content-Type" content="text/html;charset=utf8"/>
            <title>
                <xsl:value-of select="/html/head/title"/>
            </title>
            <link rel="stylesheet" href="../toc.css" type="text/css"/>
        </head>
    </xsl:template>

    <xsl:template match="body">
        <body>
            <p class="JMTitleHeading">
                <xsl:value-of select="/html/head/title"/>
            </p>
            <xsl:apply-templates/>
        </body>
    </xsl:template>

    <xsl:template match="div">
        <xsl:choose>
            <xsl:when test="@class='navheader'"/>
            <xsl:when test="@class='navfooter'"/>
            <xsl:when test="@class='book'">
                <xsl:apply-templates/>
            </xsl:when>
            <xsl:when test="@class='titlepage'"/>
            <xsl:when test="@class='toc'">
                <div class="toc">
                    <xsl:copy-of select="*[2]"/>
                </div>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>