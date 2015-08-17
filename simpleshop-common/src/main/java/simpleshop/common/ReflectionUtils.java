package simpleshop.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.HashSet;

/**
 * Most reflection api calls are encapsulated in this class (as long as the logic does not depend on other parts of the project).
 */
public final class ReflectionUtils {

    private ReflectionUtils(){}
    private static final HashSet<String> primitiveConversions = new HashSet<>();

    static {
        String[] primitiveTypes = {"short", "Short", "int", "Integer", "long", "Long"};

        for(int i=0; i<primitiveTypes.length - 1; i++){
            for(int j=i+1; j<primitiveTypes.length; j++){

                primitiveConversions.add(primitiveTypes[i] + "-" + primitiveTypes[j]);
                primitiveConversions.add(primitiveTypes[j] + "-" + primitiveTypes[i]);
            }
        }
    }

    /**
     * Check if a method is an public instance getter. Java built in getters are excluded.
     * @param method method.
     * @return true if yes.
     */
    public static boolean isPublicInstanceGetter(Method method) {
        return method.getParameterTypes().length == 0
                && method.getName().startsWith("get")
                && !method.getName().equals("getClass")
                && !method.getName().equals("getBytes")
                && Modifier.isPublic(method.getModifiers())
                && !Modifier.isStatic(method.getModifiers());
    }

    /**
     * Get a getter that can accept argClass.
     * @param clazz find a getter in this class.
     * @param propertyName the getter property name.
     * @param argClass class of the value to pass to the setter.
     * @return the first setter that meets this criteria.
     */
    public static Method getSetter(Class<?> clazz, String propertyName, Class<?> argClass){
        String setterName = "set" + StringUtils.firstCharUpper(propertyName);
        Method[] methods = clazz.getMethods();
        for (Method method : methods){
            if(setterName.equals(method.getName()) ){
                if(method.getParameterTypes().length == 1){
                    if(argClass == null || isAssignableFrom(argClass, method.getParameterTypes()[0])){
                        return method;
                    }
                }
            }
        }
        return null;
    }

    /**
     * isAssignableFrom in Class type is inadequate when it comes to primitives.
     * @param fromClass from class.
     * @param toClass to class.
     * @return true if can.
     */
    public static boolean isAssignableFrom(Class<?> fromClass, Class<?> toClass){
        if(toClass.isPrimitive() || fromClass.isPrimitive()){
            return primitiveConversions.contains(toClass.getSimpleName() + "-" + fromClass.getSimpleName());
        }
        return toClass.isAssignableFrom(fromClass);
    }

     public static <T> T parseString(String s, Class<T> returnType) {

         if( returnType == Boolean.class || returnType == Character.class || returnType == Short.class || returnType == Integer.class || returnType == Float.class || returnType == Double.class || returnType == BigDecimal.class || returnType == String.class) {
             try {
                 return returnType.getConstructor(new Class[]{String.class}).newInstance(s);
             } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                 throw new RuntimeException(ex);
             }
         }
         throw new IllegalArgumentException("returnType"); //not supported
    }

    @SuppressWarnings("unchecked")
    public static <T> T  parseObject(Object value, Class<T> returnType){

        if(value == null)
            return null;

        if(returnType == value.getClass())
            return (T)value;

        return ReflectionUtils.parseString(value.toString(), returnType);
    }

    /**
     * Set the value of a deep property.
     *
     * @param obj      the this object.
     * @param property property path, i.e dot separated properties.
     * @param value    set the property value.
     * @return true if property is set, otherwise false.
     */
    public static boolean setProperty(Object obj, String property, Object value) {
        if(obj == null)
            return false;

        int lastIndex = property.lastIndexOf('.');
        if (lastIndex >= 0) {
            obj = getProperty(obj, property.substring(0, lastIndex));
            if (obj == null)
                return false;
            property = property.substring(lastIndex + 1);
        }
        Method setter = ReflectionUtils.getSetter(obj.getClass(), property, value == null ? null : value.getClass());
        if (setter != null) {
            try{
                setter.invoke(obj, value);
                return true;
            }catch (IllegalAccessException | InvocationTargetException ex){
                throw new RuntimeException(ex);
            }
        }
        return false;
    }

    /**
     * Get the value of a deep property.
     *
     * @param obj      the this object.
     * @param property property path, i.e dot separated properties.
     * @return the property value.
     */
    public static Object getProperty(Object obj, String property) {
        if (obj == null)
            return null;

        String[] path = property.split("\\.");
        boolean tryIs = false; //getBoolean v.s. isBoolean
        for (int i = 0; i < path.length; i++) {
            String prop = path[i];
            String getterName = (tryIs ? "is" : "get") + StringUtils.firstCharUpper(prop);
            try {
                Method getter = obj.getClass().getMethod(getterName);
                obj = getter.invoke(obj);
                if (obj == null)
                    return null;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                if(i == path.length - 1 && !tryIs){
                    tryIs = true;
                    i--;
                    continue;
                }
                throw new RuntimeException(ex);
            }
        }
        return obj;
    }


}
