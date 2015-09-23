package com.yatatsu.autobundle.processor.data;


public class NotPublicConstructorConverter implements SourceBase {

        @Override
        public String getTargetClassName() {
                return "com.yatatsu.autobundle.example.ExampleActivity";
        }

        @Override
        public String getTargetSource() {
                return "package com.yatatsu.autobundle.example;\n" +
                        "\n" +
                        "import android.app.Activity;\n" +
                        "\n" +
                        "import com.yatatsu.autobundle.Arg;\n" +
                        "import com.yatatsu.autobundle.AutoBundleTarget;\n" +
                        "import com.yatatsu.autobundle.Converter;\n" +
                        "\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "@AutoBundleTarget\n" +
                        "public class ExampleActivity extends Activity {\n" +
                        "\n" +
                        "@Arg(converter = DateArgConverter.class)\n" +
                        "Date date;\n" +
                        "\n" +
                        "public static class DateArgConverter implements Converter<Date, Long> {\n" +
                        "\n" +
                        "private DateArgConverter() {}\n" +
                        "\n" +
                        "@Override\n" +
                        "public Long convert(Date o) {\n" +
                        "return o.getTime();\n" +
                        "}\n" +
                        "\n" +
                        "@Override\n" +
                        "public Date original(Long s) {\n" +
                        "return new Date(s);\n" +
                        "}\n" +
                        "}\n" +
                        "}";
        }

        @Override
        public String getExpectClassName() {
                return "";
        }

        @Override
        public String getExpectSource() {
                return "";
        }
}
