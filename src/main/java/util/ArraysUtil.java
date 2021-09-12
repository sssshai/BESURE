package util;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.io.UnsupportedEncodingException;

public class ArraysUtil {
    /**
     * 合并字节数组
     *
     * @param values 要合并的字节数组
     * @return 合并过后的字节数组
     */
    public static byte[] mergeByte(byte[]... values) {
        int byte_length = 0;
        for (byte[] bytes : values) {
            byte_length += bytes.length;
        }

        byte[] mergedByte = new byte[byte_length];
        int countlength = 0;
        for (byte[] value : values) {
            System.arraycopy(value, 0, mergedByte, countlength, value.length);
            countlength += value.length;
        }
        return mergedByte;
    }

    /**
     * 分割数组
     *
     * @param value 分割的字节数组
     * @param size  分割的个数
     */
    public static byte[][] splitByte(byte[] value, int... size) {
        byte[][] result = new byte[size.length][];

        int index = 0;
        int i, j;
        for (i = 0; i < size.length; i++) {
            byte[] mid_data = new byte[size[i]];
            for (j = 0; j < size[i]; j++) {
                mid_data[j] = value[index];
                index++;
            }
            result[i] = mid_data;
        }
        return result;
    }

    public static boolean isEqual(byte[] b1, byte[] b2) {
        if (b1 == null && b2 == null) return true;
        if (b1 == null || b2 == null) return false;
        if (b1.length != b2.length) return false;
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }
        return true;
    }

    public static ElementString elementToString(Element[] elements) {
        int n = elements.length;
        ElementString es = new ElementString();
        es.setNumber(n);
        StringBuilder res = new StringBuilder();
        int number = 0;
        String[] elementString = new String[n];

        try {
            for (int i = 1; i < n; i++) {
                elementString[i] = new String(elements[i].toBytes(), "ISO8859-1");
                number = elementString[i].length();
                res.append(elementString[i]);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        es.setElements(res.toString());
        es.setLength(number);

        return es;
    }

    /**
     * 默认G1
     */
    public static Element[] stringToElement(Pairing pairing, ElementString es) {
        int n = es.number;
        String[] strings = new String[n];
        Element[] elements = new Element[n];
        strings[1] = es.getElements().substring(0, es.getLength());
        for (int i = 2; i < n; i++) {
            strings[i] = es.getElements().substring(es.getLength() * (i - 1), es.getLength() * i);
        }
        try {
            for (int i = 1; i < n; i++) {
                elements[i] = pairing.getG1().newElementFromBytes(strings[i].getBytes("ISO8859-1"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return elements;
    }

    public static class ElementString {
        public String elements;
        public int length;
        public int number;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "ElementString{" +
                    "elements='" + elements + '\'' +
                    ", length=" + length +
                    ", number=" + number +
                    '}';
        }

        public String getElements() {
            return elements;
        }

        public void setElements(String elements) {
            this.elements = elements;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }
}
