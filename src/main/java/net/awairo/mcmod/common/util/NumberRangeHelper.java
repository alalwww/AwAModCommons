/*
 * (c) 2013 alalwww
 * https://github.com/alalwww
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 *
 * この MOD は、Minecraft Mod Public License (MMPL) 1.0 の条件のもとに配布されています。
 * ライセンスの内容は次のサイトを確認してください。 http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.awairo.mcmod.common.util;

import static com.google.common.base.Preconditions.*;

/**
 * 範囲を持った数値ヘルパー.
 * 
 * @author alalwww
 * 
 */
public final class NumberRangeHelper
{
    private NumberRangeHelper()
    {
    }

    /** 範囲を持った数値. */
    public static interface RangedNumber<V extends Number>
    {
        /**
         * 現在の値を取得します.
         * 
         * @return 現在の値
         */
        V current();

        /**
         * より上の値に変更します.
         * 
         * @return より上の値があり変更できた場合 true
         */
        boolean toGreater();

        /**
         * より下の値に変更します.
         * 
         * @return より下の値があり変更できた場合 true
         */
        boolean toLesser();

        /**
         * 最小値を設定します.
         * 
         * @param min
         * @return インスタンス
         */
        RangedNumber<V> setMin(V min);

        /**
         * 最大値を設定します.
         * 
         * @param max
         *            最大値
         * @return インスタンス
         */
        RangedNumber<V> setMax(V max);

        /**
         * 増分値を設定します.
         * 
         * @param incrementalValue
         *            増分値
         * @return インスタンス
         */
        RangedNumber<V> setIncrementalValue(V incrementalValue);

        /**
         * 現在の値を設定します.
         * 
         * @param currentValue
         *            現在の値
         * @return インスタンス
         */
        RangedNumber<V> setCurrentValue(V currentValue);
    }

    /**
     * 増加値1で初期値がmin～maxの間の範囲を持った、32bit符号付き整数を生成します.
     * 
     * @param min
     *            最小値
     * @param max
     *            最大値
     * @return 範囲を持ったInteger値
     */
    public static RangedNumber<Integer> create(int min, int max)
    {
        return create(min, max, 1, (max - min) / 2 + min);
    }

    /**
     * 範囲を持った32bit符号付き整数を生成します.
     * 
     * @param min
     *            最小値
     * @param max
     *            最大値
     * @param incrementalValue
     *            増加値
     * @param defaultValue
     *            初期値
     * @return 範囲を持ったInteger値
     */
    public static RangedNumber<Integer> create(int min, int max, int incrementalValue, int defaultValue)
    {
        return new SimpleRangedNumber<Integer>(min, max, incrementalValue, defaultValue)
        {
            @Override
            public boolean hasGreater()
            {
                return currentValue < max;
            }

            @Override
            public boolean hasLesser()
            {
                return currentValue > min;
            }

            @Override
            protected Integer getGreaterValue()
            {
                return currentValue + incrementalValue;
            }

            @Override
            protected Integer getLesserValue()
            {
                return currentValue - incrementalValue;
            }
        };
    }

    /**
     * 範囲を持った整数の単純な抽象クラス.
     * 
     * @author alalwww
     */
    public static abstract class SimpleRangedNumber<V extends Number> implements RangedNumber<V>
    {
        protected V min;
        protected V max;
        protected V currentValue;
        protected V incrementalValue;

        /**
         * Constructor.
         * 
         * @param min
         *            最小値
         * @param max
         *            最大値
         * @param incrementalValue
         *            増加値
         * @param defaultValue
         *            初期値
         */
        protected SimpleRangedNumber(V min, V max, V incrementalValue, V defaultValue)
        {
            setMin(min);
            setMax(max);
            setIncrementalValue(incrementalValue);
            setCurrentValue(defaultValue);
        }

        @Override
        public V current()
        {
            return currentValue;
        }

        @Override
        public boolean toGreater()
        {
            if (hasGreater())
            {
                currentValue = getGreaterValue();
                return true;
            }
            return false;
        }

        @Override
        public boolean toLesser()
        {
            if (hasLesser())
            {
                currentValue = getLesserValue();
                return true;
            }
            return false;
        }

        protected abstract boolean hasGreater();

        protected abstract boolean hasLesser();

        protected abstract V getGreaterValue();

        protected abstract V getLesserValue();

        @Override
        public RangedNumber<V> setMin(V min)
        {
            this.min = checkNotNull(min);
            return this;
        }

        @Override
        public RangedNumber<V> setMax(V max)
        {
            this.max = checkNotNull(max);
            return this;
        }

        @Override
        public RangedNumber<V> setIncrementalValue(V incrementalValue)
        {
            this.incrementalValue = checkNotNull(incrementalValue);
            return this;
        }

        @Override
        public RangedNumber<V> setCurrentValue(V currentValue)
        {
            this.currentValue = checkNotNull(currentValue);
            return this;
        }
    }
}
