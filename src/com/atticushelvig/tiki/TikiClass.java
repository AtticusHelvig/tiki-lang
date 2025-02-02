package com.atticushelvig.tiki;

import java.util.List;
import java.util.Map;

class TikiClass implements TikiCallable {
    final String name;
    final TikiClass superclass;
    private final Map<String, TikiFunction> methods;

    TikiClass(String name, TikiClass superclass, Map<String, TikiFunction> methods) {
        this.name = name;
        this.superclass = superclass;
        this.methods = methods;
    }

    TikiFunction findMethod(String name) {
        if (methods.containsKey(name)) {
            return methods.get(name);
        }
        if (superclass != null) {
            return superclass.findMethod(name);
        }
        return null;
    }

    @Override
    public int arity() {
        TikiFunction initializer = findMethod("init");
        if (initializer == null) {
            return 0;
        }
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        TikiInstance instance = new TikiInstance(this);
        TikiFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    @Override
    public String toString() {
        return name;
    }
}
