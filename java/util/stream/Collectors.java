package java.util.stream;

import java.util.concurrent.*;
import java.util.function.*;
import java.util.*;

public final class Collectors
{
    static final Set<Collector.Characteristics> CH_CONCURRENT_ID;
    static final Set<Collector.Characteristics> CH_CONCURRENT_NOID;
    static final Set<Collector.Characteristics> CH_ID;
    static final Set<Collector.Characteristics> CH_UNORDERED_ID;
    static final Set<Collector.Characteristics> CH_NOID;
    
    private Collectors() {
    }
    
    private static <T> BinaryOperator<T> throwingMerger() {
        final IllegalStateException ex;
        return (o, p1) -> {
            new IllegalStateException(String.format("Duplicate key %s", o));
            throw ex;
        };
    }
    
    private static <I, R> Function<I, R> castingIdentity() {
        return (Function<I, R>)(o -> o);
    }
    
    public static <T, C extends Collection<T>> Collector<T, ?, C> toCollection(final Supplier<C> supplier) {
        return new CollectorImpl<T, Object, C>((Supplier<?>)supplier, Collection::add, (collection, collection2) -> {
            collection.addAll(collection2);
            return collection;
        }, Collectors.CH_ID);
    }
    
    public static <T> Collector<T, ?, List<T>> toList() {
        return new CollectorImpl<T, Object, List<T>>((Supplier<?>)ArrayList::new, List::add, (list, list2) -> {
            list.addAll(list2);
            return list;
        }, Collectors.CH_ID);
    }
    
    public static <T> Collector<T, ?, Set<T>> toSet() {
        return new CollectorImpl<T, Object, Set<T>>((Supplier<?>)HashSet::new, Set::add, (set, set2) -> {
            set.addAll(set2);
            return set;
        }, Collectors.CH_UNORDERED_ID);
    }
    
    public static Collector<CharSequence, ?, String> joining() {
        return new CollectorImpl<CharSequence, Object, String>((Supplier<?>)StringBuilder::new, StringBuilder::append, (sb, sb2) -> {
            sb.append((CharSequence)sb2);
            return sb;
        }, StringBuilder::toString, Collectors.CH_NOID);
    }
    
    public static Collector<CharSequence, ?, String> joining(final CharSequence charSequence) {
        return joining(charSequence, "", "");
    }
    
    public static Collector<CharSequence, ?, String> joining(final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3) {
        return new CollectorImpl<CharSequence, Object, String>(() -> new StringJoiner(charSequence, charSequence2, charSequence3), StringJoiner::add, StringJoiner::merge, StringJoiner::toString, Collectors.CH_NOID);
    }
    
    private static <K, V, M extends Map<K, V>> BinaryOperator<M> mapMerger(final BinaryOperator<V> binaryOperator) {
        final Iterator<Map.Entry<Object, V>> iterator;
        Map.Entry<Object, V> entry;
        return (BinaryOperator<M>)((map, map2) -> {
            map2.entrySet().iterator();
            while (iterator.hasNext()) {
                entry = iterator.next();
                map.merge(entry.getKey(), entry.getValue(), (BiFunction<? super V, ? super V, ? extends V>)binaryOperator);
            }
            return map;
        });
    }
    
    public static <T, U, A, R> Collector<T, ?, R> mapping(final Function<? super T, ? extends U> function, final Collector<? super U, A, R> collector) {
        return new CollectorImpl<T, Object, R>(collector.supplier(), (a, o) -> collector.accumulator().accept(a, (Object)function.apply((Object)o)), collector.combiner(), collector.finisher(), collector.characteristics());
    }
    
    public static <T, A, R, RR> Collector<T, A, RR> collectingAndThen(final Collector<T, A, R> collector, final Function<R, RR> function) {
        Set<Collector.Characteristics> set = collector.characteristics();
        if (set.contains(Collector.Characteristics.IDENTITY_FINISH)) {
            if (set.size() == 1) {
                set = Collectors.CH_NOID;
            }
            else {
                final EnumSet<Enum> copy = EnumSet.copyOf((Collection<Enum>)set);
                copy.remove(Collector.Characteristics.IDENTITY_FINISH);
                set = (Set<Collector.Characteristics>)Collections.unmodifiableSet((Set<?>)copy);
            }
        }
        return new CollectorImpl<T, A, RR>((Supplier<Object>)collector.supplier(), (BiConsumer<Object, Object>)collector.accumulator(), (BinaryOperator<Object>)collector.combiner(), (Function<Object, Object>)collector.finisher().andThen((Function<? super R, ?>)function), set);
    }
    
    public static <T> Collector<T, ?, Long> counting() {
        return reducing(0L, p0 -> 1L, Long::sum);
    }
    
    public static <T> Collector<T, ?, Optional<T>> minBy(final Comparator<? super T> comparator) {
        return reducing(BinaryOperator.minBy(comparator));
    }
    
    public static <T> Collector<T, ?, Optional<T>> maxBy(final Comparator<? super T> comparator) {
        return reducing(BinaryOperator.maxBy(comparator));
    }
    
    public static <T> Collector<T, ?, Integer> summingInt(final ToIntFunction<? super T> toIntFunction) {
        final int n;
        final int n2;
        return new CollectorImpl<T, Object, Integer>(() -> new int[1], (array3, o) -> array3[n] += toIntFunction.applyAsInt((Object)o), (array, array4) -> {
            array[n2] += array4[0];
            return array;
        }, array2 -> array2[0], Collectors.CH_NOID);
    }
    
    public static <T> Collector<T, ?, Long> summingLong(final ToLongFunction<? super T> toLongFunction) {
        final int n;
        final int n2;
        return new CollectorImpl<T, Object, Long>(() -> new long[1], (array3, o) -> array3[n] += toLongFunction.applyAsLong((Object)o), (array, array4) -> {
            array[n2] += array4[0];
            return array;
        }, array2 -> array2[0], Collectors.CH_NOID);
    }
    
    public static <T> Collector<T, ?, Double> summingDouble(final ToDoubleFunction<? super T> toDoubleFunction) {
        final int n;
        final int n2;
        return new CollectorImpl<T, Object, Double>(() -> new double[3], (array3, o) -> {
            sumWithCompensation(array3, toDoubleFunction.applyAsDouble((Object)o));
            array3[n] += toDoubleFunction.applyAsDouble((Object)o);
        }, (array, array4) -> {
            sumWithCompensation(array, array4[0]);
            array[n2] += array4[2];
            return sumWithCompensation(array, array4[1]);
        }, array2 -> computeFinalSum(array2), Collectors.CH_NOID);
    }
    
    static double[] sumWithCompensation(final double[] array, final double n) {
        final double n2 = n - array[1];
        final double n3 = array[0];
        final double n4 = n3 + n2;
        array[1] = n4 - n3 - n2;
        array[0] = n4;
        return array;
    }
    
    static double computeFinalSum(final double[] array) {
        final double n = array[0] + array[1];
        final double n2 = array[array.length - 1];
        if (Double.isNaN(n) && Double.isInfinite(n2)) {
            return n2;
        }
        return n;
    }
    
    public static <T> Collector<T, ?, Double> averagingInt(final ToIntFunction<? super T> toIntFunction) {
        final int n;
        final int n2;
        final int n3;
        final int n4;
        return new CollectorImpl<T, Object, Double>(() -> new long[2], (array3, o) -> {
            array3[n] += toIntFunction.applyAsInt((Object)o);
            ++array3[n2];
        }, (array, array4) -> {
            array[n3] += array4[0];
            array[n4] += array4[1];
            return array;
        }, array2 -> (array2[1] == 0L) ? 0.0 : (array2[0] / (double)array2[1]), Collectors.CH_NOID);
    }
    
    public static <T> Collector<T, ?, Double> averagingLong(final ToLongFunction<? super T> toLongFunction) {
        final int n;
        final int n2;
        final int n3;
        final int n4;
        return new CollectorImpl<T, Object, Double>(() -> new long[2], (array3, o) -> {
            array3[n] += toLongFunction.applyAsLong((Object)o);
            ++array3[n2];
        }, (array, array4) -> {
            array[n3] += array4[0];
            array[n4] += array4[1];
            return array;
        }, array2 -> (array2[1] == 0L) ? 0.0 : (array2[0] / (double)array2[1]), Collectors.CH_NOID);
    }
    
    public static <T> Collector<T, ?, Double> averagingDouble(final ToDoubleFunction<? super T> toDoubleFunction) {
        final int n;
        final int n2;
        final int n3;
        final int n4;
        return new CollectorImpl<T, Object, Double>(() -> new double[4], (array3, o) -> {
            sumWithCompensation(array3, toDoubleFunction.applyAsDouble((Object)o));
            ++array3[n];
            array3[n2] += toDoubleFunction.applyAsDouble((Object)o);
        }, (array, array4) -> {
            sumWithCompensation(array, array4[0]);
            sumWithCompensation(array, array4[1]);
            array[n3] += array4[2];
            array[n4] += array4[3];
            return array;
        }, array2 -> (array2[2] == 0.0) ? 0.0 : (computeFinalSum(array2) / array2[2]), Collectors.CH_NOID);
    }
    
    public static <T> Collector<T, ?, T> reducing(final T t, final BinaryOperator<T> binaryOperator) {
        return new CollectorImpl<T, Object, T>((Supplier<?>)boxSupplier(t), (array2, t2) -> array2[0] = binaryOperator.apply((T)array2[0], t2), (array3, array4) -> {
            array3[0] = binaryOperator.apply((T)array3[0], (T)array4[0]);
            return array3;
        }, array -> array[0], Collectors.CH_NOID);
    }
    
    private static <T> Supplier<T[]> boxSupplier(final T t) {
        return (Supplier<T[]>)(() -> new Object[] { t });
    }
    
    public static <T> Collector<T, ?, Optional<T>> reducing(final BinaryOperator<T> binaryOperator) {
        class OptionalBox implements Consumer<T>
        {
            T value;
            boolean present;
            
            OptionalBox() {
                this.value = null;
                this.present = false;
            }
            
            @Override
            public void accept(final T value) {
                if (this.present) {
                    this.value = (T)binaryOperator.apply(this.value, value);
                }
                else {
                    this.value = value;
                    this.present = true;
                }
            }
        }
        return new CollectorImpl<T, Object, Optional<T>>(() -> new OptionalBox(), OptionalBox::accept, (optionalBox, optionalBox3) -> {
            if (optionalBox3.present) {
                optionalBox.accept(optionalBox3.value);
            }
            return optionalBox;
        }, optionalBox2 -> Optional.ofNullable(optionalBox2.value), Collectors.CH_NOID);
    }
    
    public static <T, U> Collector<T, ?, U> reducing(final U u, final Function<? super T, ? extends U> function, final BinaryOperator<U> binaryOperator) {
        return new CollectorImpl<T, Object, U>((Supplier<?>)boxSupplier(u), (array3, o) -> array3[0] = binaryOperator.apply((U)array3[0], (U)function.apply((Object)o)), (array2, array4) -> {
            array2[0] = binaryOperator.apply((U)array2[0], (U)array4[0]);
            return array2;
        }, array -> array[0], Collectors.CH_NOID);
    }
    
    public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(final Function<? super T, ? extends K> function) {
        return groupingBy(function, (Collector<? super T, ?, List<T>>)toList());
    }
    
    public static <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(final Function<? super T, ? extends K> function, final Collector<? super T, A, D> collector) {
        return groupingBy((Function<? super T, ?>)function, (Supplier<Map<K, D>>)HashMap::new, collector);
    }
    
    public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> groupingBy(final Function<? super T, ? extends K> function, final Supplier<M> supplier, final Collector<? super T, A, D> collector) {
        final BiConsumer<Object, Object> biConsumer = (map2, o) -> collector.accumulator().accept(map2.computeIfAbsent(Objects.requireNonNull((Object)function.apply((Object)o), "element cannot be mapped to a null key"), p1 -> collector.supplier().get()), o);
        final BinaryOperator<Object> mapMerger = mapMerger(collector.combiner());
        if (collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl<T, Object, M>((Supplier<Object>)supplier, biConsumer, mapMerger, Collectors.CH_ID);
        }
        return new CollectorImpl<T, Object, M>((Supplier<Object>)supplier, biConsumer, mapMerger, map -> {
            map.replaceAll((p1, a) -> collector.finisher().apply(a));
            return map;
        }, Collectors.CH_NOID);
    }
    
    public static <T, K> Collector<T, ?, ConcurrentMap<K, List<T>>> groupingByConcurrent(final Function<? super T, ? extends K> function) {
        return groupingByConcurrent((Function<? super T, ?>)function, (Supplier<ConcurrentMap<K, List<T>>>)ConcurrentHashMap::new, (Collector<? super T, ?, List<? super T>>)toList());
    }
    
    public static <T, K, A, D> Collector<T, ?, ConcurrentMap<K, D>> groupingByConcurrent(final Function<? super T, ? extends K> function, final Collector<? super T, A, D> collector) {
        return groupingByConcurrent((Function<? super T, ?>)function, (Supplier<ConcurrentMap<K, D>>)ConcurrentHashMap::new, collector);
    }
    
    public static <T, K, A, D, M extends ConcurrentMap<K, D>> Collector<T, ?, M> groupingByConcurrent(final Function<? super T, ? extends K> function, final Supplier<M> supplier, final Collector<? super T, A, D> collector) {
        collector.supplier();
        collector.accumulator();
        final BinaryOperator<Object> mapMerger = mapMerger(collector.combiner());
        BiConsumer<Object, Object> biConsumer;
        if (collector.characteristics().contains(Collector.Characteristics.CONCURRENT)) {
            final BiConsumer<Object, Object> biConsumer2;
            final Supplier<A> supplier2;
            biConsumer = ((concurrentMap2, o) -> biConsumer2.accept(concurrentMap2.computeIfAbsent(Objects.requireNonNull((Object)function.apply((Object)o), "element cannot be mapped to a null key"), p1 -> supplier2.get()), o));
        }
        else {
            final Supplier<Object> supplier3;
            final Object o3;
            final BiConsumer<Object, Object> biConsumer3;
            biConsumer = ((concurrentMap3, o2) -> {
                concurrentMap3.computeIfAbsent(Objects.requireNonNull((Object)function.apply((Object)o2), "element cannot be mapped to a null key"), p1 -> supplier3.get());
                synchronized (o3) {
                    biConsumer3.accept(o3, o2);
                }
                return;
            });
        }
        if (collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl<T, Object, M>((Supplier<Object>)supplier, biConsumer, mapMerger, Collectors.CH_CONCURRENT_ID);
        }
        return new CollectorImpl<T, Object, M>((Supplier<Object>)supplier, biConsumer, mapMerger, concurrentMap -> {
            concurrentMap.replaceAll((p1, a) -> collector.finisher().apply(a));
            return concurrentMap;
        }, Collectors.CH_CONCURRENT_NOID);
    }
    
    public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(final Predicate<? super T> predicate) {
        return partitioningBy(predicate, (Collector<? super T, ?, List<T>>)toList());
    }
    
    public static <T, D, A> Collector<T, ?, Map<Boolean, D>> partitioningBy(final Predicate<? super T> predicate, final Collector<? super T, A, D> collector) {
        final BiConsumer<Object, Object> biConsumer = (partition3, o) -> collector.accumulator().accept((A)(predicate.test((Object)o) ? partition3.forTrue : partition3.forFalse), o);
        final BiFunction<T, T, Object> biFunction2;
        final BiFunction<Object, Object, Partition> biFunction = (partition, partition4) -> {
            collector.combiner();
            return new Partition(biFunction2.apply(partition.forTrue, partition4.forTrue), biFunction2.apply(partition.forFalse, partition4.forFalse));
        };
        final Supplier<Partition> supplier = () -> new Partition(collector.supplier().get(), collector.supplier().get());
        if (collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH)) {
            return new CollectorImpl<T, Object, Map<Boolean, D>>((Supplier<Object>)supplier, biConsumer, (BinaryOperator<Object>)biFunction, Collectors.CH_ID);
        }
        return new CollectorImpl<T, Object, Map<Boolean, D>>((Supplier<Object>)supplier, biConsumer, (BinaryOperator<Object>)biFunction, partition2 -> new Partition(collector.finisher().apply((A)partition2.forTrue), collector.finisher().apply((A)partition2.forFalse)), Collectors.CH_NOID);
    }
    
    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(final Function<? super T, ? extends K> function, final Function<? super T, ? extends U> function2) {
        return toMap((Function<? super T, ?>)function, (Function<? super T, ?>)function2, throwingMerger(), (Supplier<Map<K, U>>)HashMap::new);
    }
    
    public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(final Function<? super T, ? extends K> function, final Function<? super T, ? extends U> function2, final BinaryOperator<U> binaryOperator) {
        return toMap((Function<? super T, ?>)function, function2, binaryOperator, (Supplier<Map<K, U>>)HashMap::new);
    }
    
    public static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(final Function<? super T, ? extends K> function, final Function<? super T, ? extends U> function2, final BinaryOperator<U> binaryOperator, final Supplier<M> supplier) {
        return new CollectorImpl<T, Object, M>(supplier, (map, o) -> map.merge(function.apply((Object)o), function2.apply((Object)o), (BiFunction<? super Object, ? super Object, ?>)binaryOperator), mapMerger(binaryOperator), Collectors.CH_ID);
    }
    
    public static <T, K, U> Collector<T, ?, ConcurrentMap<K, U>> toConcurrentMap(final Function<? super T, ? extends K> function, final Function<? super T, ? extends U> function2) {
        return toConcurrentMap((Function<? super T, ?>)function, (Function<? super T, ?>)function2, throwingMerger(), (Supplier<ConcurrentMap<K, U>>)ConcurrentHashMap::new);
    }
    
    public static <T, K, U> Collector<T, ?, ConcurrentMap<K, U>> toConcurrentMap(final Function<? super T, ? extends K> function, final Function<? super T, ? extends U> function2, final BinaryOperator<U> binaryOperator) {
        return toConcurrentMap((Function<? super T, ?>)function, function2, binaryOperator, (Supplier<ConcurrentMap<K, U>>)ConcurrentHashMap::new);
    }
    
    public static <T, K, U, M extends ConcurrentMap<K, U>> Collector<T, ?, M> toConcurrentMap(final Function<? super T, ? extends K> function, final Function<? super T, ? extends U> function2, final BinaryOperator<U> binaryOperator, final Supplier<M> supplier) {
        return new CollectorImpl<T, Object, M>(supplier, (concurrentMap, o) -> concurrentMap.merge(function.apply((Object)o), function2.apply((Object)o), (BiFunction<? super Object, ? super Object, ?>)binaryOperator), mapMerger(binaryOperator), Collectors.CH_CONCURRENT_ID);
    }
    
    public static <T> Collector<T, ?, IntSummaryStatistics> summarizingInt(final ToIntFunction<? super T> toIntFunction) {
        return new CollectorImpl<T, Object, IntSummaryStatistics>((Supplier<?>)IntSummaryStatistics::new, (intSummaryStatistics2, o) -> intSummaryStatistics2.accept(toIntFunction.applyAsInt((Object)o)), (intSummaryStatistics, intSummaryStatistics3) -> {
            intSummaryStatistics.combine(intSummaryStatistics3);
            return intSummaryStatistics;
        }, Collectors.CH_ID);
    }
    
    public static <T> Collector<T, ?, LongSummaryStatistics> summarizingLong(final ToLongFunction<? super T> toLongFunction) {
        return new CollectorImpl<T, Object, LongSummaryStatistics>((Supplier<?>)LongSummaryStatistics::new, (longSummaryStatistics2, o) -> longSummaryStatistics2.accept(toLongFunction.applyAsLong((Object)o)), (longSummaryStatistics, longSummaryStatistics3) -> {
            longSummaryStatistics.combine(longSummaryStatistics3);
            return longSummaryStatistics;
        }, Collectors.CH_ID);
    }
    
    public static <T> Collector<T, ?, DoubleSummaryStatistics> summarizingDouble(final ToDoubleFunction<? super T> toDoubleFunction) {
        return new CollectorImpl<T, Object, DoubleSummaryStatistics>((Supplier<?>)DoubleSummaryStatistics::new, (doubleSummaryStatistics2, o) -> doubleSummaryStatistics2.accept(toDoubleFunction.applyAsDouble((Object)o)), (doubleSummaryStatistics, doubleSummaryStatistics3) -> {
            doubleSummaryStatistics.combine(doubleSummaryStatistics3);
            return doubleSummaryStatistics;
        }, Collectors.CH_ID);
    }
    
    static {
        CH_CONCURRENT_ID = Collections.unmodifiableSet((Set<? extends Collector.Characteristics>)EnumSet.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
        CH_CONCURRENT_NOID = Collections.unmodifiableSet((Set<? extends Collector.Characteristics>)EnumSet.of(Collector.Characteristics.CONCURRENT, Collector.Characteristics.UNORDERED));
        CH_ID = Collections.unmodifiableSet((Set<? extends Collector.Characteristics>)EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
        CH_UNORDERED_ID = Collections.unmodifiableSet((Set<? extends Collector.Characteristics>)EnumSet.of(Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH));
        CH_NOID = Collections.emptySet();
    }
    
    static class CollectorImpl<T, A, R> implements Collector<T, A, R>
    {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Characteristics> characteristics;
        
        CollectorImpl(final Supplier<A> supplier, final BiConsumer<A, T> accumulator, final BinaryOperator<A> combiner, final Function<A, R> finisher, final Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }
        
        CollectorImpl(final Supplier<A> supplier, final BiConsumer<A, T> biConsumer, final BinaryOperator<A> binaryOperator, final Set<Characteristics> set) {
            this(supplier, biConsumer, binaryOperator, castingIdentity(), set);
        }
        
        @Override
        public BiConsumer<A, T> accumulator() {
            return this.accumulator;
        }
        
        @Override
        public Supplier<A> supplier() {
            return this.supplier;
        }
        
        @Override
        public BinaryOperator<A> combiner() {
            return this.combiner;
        }
        
        @Override
        public Function<A, R> finisher() {
            return this.finisher;
        }
        
        @Override
        public Set<Characteristics> characteristics() {
            return this.characteristics;
        }
    }
    
    private static final class Partition<T> extends AbstractMap<Boolean, T> implements Map<Boolean, T>
    {
        final T forTrue;
        final T forFalse;
        
        Partition(final T forTrue, final T forFalse) {
            this.forTrue = forTrue;
            this.forFalse = forFalse;
        }
        
        @Override
        public Set<Entry<Boolean, T>> entrySet() {
            return new AbstractSet<Entry<Boolean, T>>() {
                @Override
                public Iterator<Entry<Boolean, T>> iterator() {
                    return Arrays.asList(new SimpleImmutableEntry(false, Partition.this.forFalse), new SimpleImmutableEntry(true, Partition.this.forTrue)).iterator();
                }
                
                @Override
                public int size() {
                    return 2;
                }
            };
        }
    }
}
