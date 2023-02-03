package indi.mofan.a48;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * @author mofan
 * @date 2023/2/3 14:37
 */
public class MutationEventTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBean(MutationEventListener.class);
        context.refresh();

        Pizza pizza = new Pizza("NewYorkPizza", 25);
        context.publishEvent(new MutationEvent<>(pizza, "ONE"));

        ChineseHamburger hamburger = new ChineseHamburger(18, "M");
        context.publishEvent(new MutationEvent<>(hamburger, "TWO"));
    }

    @Getter
    static class MutationEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {
        private static final long serialVersionUID = -2718823625228147843L;

        private final T source;

        private final String type;

        public MutationEvent(T data, String type) {
            super(data);
            this.source = data;
            this.type = type;
        }

        @Override
        public ResolvableType getResolvableType() {
            return ResolvableType.forClassWithGenerics(getClass(),
                    ResolvableType.forInstance(this.source));
        }
    }

    @Getter
    @AllArgsConstructor
    static class Pizza {
        private final String name;
        private final double price;
    }

    @Getter
    @AllArgsConstructor
    static class ChineseHamburger {
        private final double price;
        private final String size;
    }

    static class MutationEventListener {
        @EventListener
        public void handlePizza(MutationEvent<Pizza> event) {
            System.out.println("监听到 Pizza...");
            System.out.println("类型是: " + event.getType());
            Pizza pizza = event.getSource();
            System.out.println("Pizza 名称为: " + pizza.getName() + ", 价格为: " + pizza.getPrice());
        }

        @EventListener
        public void handleChineseHamburger(MutationEvent<ChineseHamburger> event) {
            System.out.println("监听到肉夹馍...");
            System.out.println("类型是: " + event.getType());
            ChineseHamburger hamburger = event.getSource();
            System.out.println("肉夹馍的价格是: " + hamburger.getPrice() + ", 大小是: " + hamburger.getSize());
        }
    }
}
