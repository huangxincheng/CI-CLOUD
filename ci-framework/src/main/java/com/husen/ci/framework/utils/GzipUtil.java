package com.husen.ci.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
public class GzipUtil {

    /**
     * 压缩数据且通过Base64加密
     *
     * @param src 源数据
     * @return
     */
    public static final String compressAndBase64(String src) {
        byte[] bytes = compress(src.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 压缩数据
     *
     * @param src 源数据
     * @return
     */
    public static final byte[] compress(String src) {
        return compress(src.getBytes());
    }

    /**
     * 压缩数据
     *
     * @param bytes 源数据
     * @return
     */
    public static final byte[] compress(byte[] bytes) {
        ByteArrayOutputStream aos = new ByteArrayOutputStream();
        GZIPOutputStream gos;
        byte[] bs = null;
        try {
            log.debug("GzipUtil 压缩前大小：" + bytes.length);
            gos = new GZIPOutputStream(aos);
            gos.write(bytes);
            gos.flush();
            gos.close();
            bs = aos.toByteArray();
            log.debug("GzipUtil 压缩后大小：" + bs.length);
        } catch (UnsupportedEncodingException e) {
            log.error("GzipUtil", e);
        } catch (IOException e) {
            log.error("GzipUtil", e);
        } finally {
            IOUtils.closeQuietly(aos);
        }
        return bs;
    }

    /**
     * 解压数据并且通过Base64解码
     *
     * @param src 源数据
     * @return
     */
    public static final String uncompressAndBase64(String src) {
        byte[] decode = Base64.getDecoder().decode(src);
        byte[] bytes = uncompress(decode);
        return new String(bytes);
    }

    /**
     * 解压缩数据
     *
     * @param src 源数据
     * @return
     */
    public static final byte[] uncompress(String src) {
        return uncompress(src.getBytes());
    }

    /**
     * 解压缩数据
     *
     * @param bs 源数据
     * @return
     */
    public static final byte[] uncompress(byte[] bs) {
        if (bs == null || bs.length == 0) {
            return null;
        }
        byte[] bytes = null;
        try(
            ByteArrayInputStream bais = new ByteArrayInputStream(bs);
            GZIPInputStream gis = new GZIPInputStream(bais)
        ) {
            bytes = IOUtils.toByteArray(gis);
        } catch (IOException e) {
            log.error("GzipUtil", e);
        }
        return bytes;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "我是人哈哈哈哈哈哈呵呵%5B%7B%22lastUpdateTime%22%3A%222011-10-28+9%3A39%3A41%22%2C%22smsList%22%3A%5B%7B%22liveState%22%3A%221AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        System.out.println("压缩前 ：" + str.getBytes().length);
        byte[] compress = compress(str);
        System.out.println("压缩后 ：" + compress.length);
        byte[] uncompress = uncompress(compress);
        System.out.println("解压后 : " + new String(uncompress).getBytes().length);
    }
}