<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

    <!-- "Document Date:"라는 레이블을 추가로 출력 -->
    <xsl:template match="pubdate" mode="titlepage.mode">
        <xsl:call-template name="paragraph">
            <xsl:with-param name="class" select="local-name(.)"/>
            <xsl:with-param name="content">
                Document Date:
                <xsl:apply-templates mode="titlepage.mode"/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

</xsl:stylesheet>
