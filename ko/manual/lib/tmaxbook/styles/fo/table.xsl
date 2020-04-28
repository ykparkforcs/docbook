<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

    <!-- for <?dbfo keep-together?> -->
    <xsl:template name="table.block">
        <xsl:param name="table.layout" select="NOTANODE"/>
        <xsl:variable name="id">
            <xsl:call-template name="object.id"/>
        </xsl:variable>
        <xsl:variable name="param.placement"
                      select="substring-after(normalize-space(
                       $formal.title.placement), concat(local-name(.), ' '))"/>
        <xsl:variable name="placement">
            <xsl:choose>
                <xsl:when test="contains($param.placement, ' ')">
                    <xsl:value-of select="substring-before($param.placement, ' ')"/>
                </xsl:when>
                <xsl:when test="$param.placement = ''">before</xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$param.placement"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <!-- keep-together 변수 선언 -->
        <xsl:variable name="keep-together">
            <xsl:call-template name="dbfo-keep-together">
                <xsl:with-param name="default">always</xsl:with-param>
            </xsl:call-template>
        </xsl:variable>

        <xsl:choose>
            <xsl:when test="self::table">
                <!-- keep-together.within-column 속성 설정 -->
                <fo:block id="{$id}"
                          xsl:use-attribute-sets="table.properties"
                          keep-together.within-column="{$keep-together}">
                    <xsl:if test="$placement = 'before'">
                        <xsl:call-template name="formal.object.heading">
                            <xsl:with-param name="placement" select="$placement"/>
                        </xsl:call-template>
                    </xsl:if>
                    <xsl:copy-of select="$table.layout"/>
                    <xsl:call-template name="table.footnote.block"/>
                    <xsl:if test="$placement != 'before'">
                        <xsl:call-template name="formal.object.heading">
                            <xsl:with-param name="placement" select="$placement"/>
                        </xsl:call-template>
                    </xsl:if>
                </fo:block>
            </xsl:when>
            <xsl:otherwise>
                <fo:block id="{$id}"
                          xsl:use-attribute-sets="informaltable.properties">
                    <xsl:copy-of select="$table.layout"/>
                    <xsl:call-template name="table.footnote.block"/>
                </fo:block>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>