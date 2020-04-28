<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version='1.0'>

    <!--
     GUI 관련 element들에 굵게 효과를 주고,
     <type> element를 monospace 글꼴로 출력한다.
     -->

    <xsl:template match="guibutton">
        <xsl:call-template name="inline.boldseq"/>
    </xsl:template>

    <xsl:template match="guiicon">
        <xsl:call-template name="inline.boldseq"/>
    </xsl:template>

    <xsl:template match="guilabel">
        <xsl:call-template name="inline.boldseq"/>
    </xsl:template>

    <xsl:template match="guimenu">
        <xsl:call-template name="inline.boldseq"/>
    </xsl:template>

    <xsl:template match="guimenuitem">
        <xsl:call-template name="inline.boldseq"/>
    </xsl:template>

    <xsl:template match="guisubmenu">
        <xsl:call-template name="inline.boldseq"/>
    </xsl:template>

    <xsl:template match="type">
        <xsl:call-template name="inline.monoseq"/>
    </xsl:template>

</xsl:stylesheet>
