<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <!-- for <?dbfo keep-together?> -->
    <xsl:template match="para">
        <xsl:variable name="keep-together">
            <xsl:call-template name="dbfo-keep-together"/>
        </xsl:variable>
        <fo:block xsl:use-attribute-sets="normal.para.spacing" keep-together.within-column="{$keep-together}">
            <xsl:call-template name="anchor"/>
            <xsl:apply-templates/>
        </fo:block>
    </xsl:template>

</xsl:stylesheet>