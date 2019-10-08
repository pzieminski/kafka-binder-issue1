package com.example.issue1;

import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
public class Issue1Application {

    public static void main(String[] args) {
        SpringApplication.run(Issue1Application.class, args);
    }

    public interface FooBindings {

        String INPUT = "foo-input";
        String OUTPUT = "foo-output";

        @Input(INPUT)
        KStream<String, String> input();

        @Output(OUTPUT)
        KStream<String, String> output();
    }

    @Profile("foo")
    @EnableBinding({ Issue1Application.FooBindings.class })
    public static class Foo {

        @StreamListener
        @SendTo(FooBindings.OUTPUT)
        public KStream<String, String> processFoo(@Input(FooBindings.INPUT) KStream<String, String> input) {
            return input;
        }

    }

    public interface BazOKBindings {

        String INPUT = "baz-input";
        String OUTPUT = "baz-output";

        @Input(INPUT)
        KStream<String, String> input();

        @Output(OUTPUT)
        KStream<String, String> output();
    }

    @Profile("baz_ok & !baz_fail")
    @EnableBinding({ BazOKBindings.class })
    public static class BazOK {

        @StreamListener
        @SendTo(BazOKBindings.OUTPUT)
        public KStream<String, String> processBoo(@Input(BazOKBindings.INPUT) KStream<String, String> input
        ) {
            return input;
        }
    }

    public interface BazFailBindings {

        String INPUT = "baz-input";
        String GLOBAL_INPUT = "baz-global-input";
        String OUTPUT = "baz-output";

        @Input(INPUT)
        KStream<String, String> input();

        @Input(GLOBAL_INPUT)
        GlobalKTable<String, String> globalInput();

        @Output(OUTPUT)
        KStream<String, String> output();
    }

    @Profile("baz_fail & !baz_ok")
    @EnableBinding({ Issue1Application.BazFailBindings.class })
    public static class BazFail {

        @StreamListener
        @SendTo(BazFailBindings.OUTPUT)
        public KStream<String, String> processBoo(@Input(BazFailBindings.INPUT) KStream<String, String> input,
                @Input(BazFailBindings.GLOBAL_INPUT) GlobalKTable<String, String> globalInput
        ) {
            return input.join(globalInput, (k, v) -> v, (v, gv) -> v);
        }
    }
}
