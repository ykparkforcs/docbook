<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

    <!--
     표지(index.html) 하단에 저작권 정보를 출력한다.
     -->
    <xsl:template name="user.footer.navigation">
        <div align="center">
            <xsl:apply-templates mode="titlepage.mode" select="bookinfo/copyright"/>
        </div>
    </xsl:template>

</xsl:stylesheet>
