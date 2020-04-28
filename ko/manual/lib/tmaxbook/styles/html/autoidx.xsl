<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE xsl:stylesheet [
<!ENTITY % common.entities SYSTEM "../../lib/docbook-xsl/common/entities.ent">
%common.entities;
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

    <!--
     출력 형식 변경
        * From
          DD (See 디플로이 디스크립터)
        * To
          DD. <i>See</i> 디플로이 디스크립터
     -->
    <xsl:template match="indexterm" mode="index-see">
        <xsl:param name="scope" select="."/>
        <xsl:param name="role" select="''"/>
        <xsl:param name="type" select="''"/>

        <xsl:text>. </xsl:text>
        <i>
            <xsl:call-template name="gentext">
                <xsl:with-param name="key" select="'see'"/>
            </xsl:call-template>
        </i>
        <xsl:text> </xsl:text>
        <xsl:value-of select="see"/>
    </xsl:template>

    <!--
     출력 형식 변경
        * From
          (See also 런타임 디플로이)
        * To
          <i>See also</i> 런타임 디플로이
     -->
    <xsl:template match="indexterm" mode="index-seealso">
        <xsl:param name="scope" select="."/>
        <xsl:param name="role" select="''"/>
        <xsl:param name="type" select="''"/>

        <xsl:for-each select="seealso">
            <xsl:sort select="translate(., &lowercase;, &uppercase;)"/>
            <dt>
                <i>
                    <xsl:call-template name="gentext">
                        <xsl:with-param name="key" select="'seealso'"/>
                    </xsl:call-template>
                </i>
                <xsl:text> </xsl:text>
                <xsl:value-of select="."/>
            </dt>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
