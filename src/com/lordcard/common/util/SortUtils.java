package com.lordcard.common.util;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序工具类
 */
public class SortUtils<T> {

	/**
	 * 排序方式
	 * ASC：升
	 * DESC：降
	 */
	public static enum Sort {
		ASC, DESC
	}

	/**
	 * 排序比较
	 * 如果参数类型为int型，则转换成int进行比较
	 * 如果两个比较的对象中只有其中一个为int，排序时则往前靠
	 * 否则toString()，然后调用String的compareTo()方法进行比较
	 */
	private static int compare(Object o1, Object o2) {
		int rank = 0;
		if (o1 instanceof Integer && o2 instanceof Integer) {
			int x = (Integer) o1;
			int y = (Integer) o2;
			rank = (x < y) ? -1 : ((x == y) ? 0 : 1);
		} else if (o1 instanceof Integer) {
			rank = -1;
		} else if (o2 instanceof Integer) {
			rank = 1;
		} else if (o1 instanceof Long && o2 instanceof Long) {
			long x = (Long) o1;
			long y = (Long) o2;
			rank = (x < y) ? -1 : ((x == y) ? 0 : 1);
		} else if (o1 instanceof Long) {
			rank = -1;
		} else if (o2 instanceof Long) {
			rank = 1;
		} else if (o1 instanceof Double && o2 instanceof Double) {
			double x = (Double) o1;
			double y = (Double) o2;
			rank = (x < y) ? -1 : ((x == y) ? 0 : 1);
		} else if (o1 instanceof Double) {
			rank = -1;
		} else if (o2 instanceof Double) {
			rank = 1;
		} else {
			rank = o1.toString().compareTo(o2.toString());
		}
		return rank;
	}

	/**
	 * 获取比较器
	 * 根据传入的字段名通过反射动态获取value并进行比较
	 * 且通过传入的sort确定排序方式
	 * 注意：如果集合中有null成员，则无论升降排序，其都将排至集合尾部
	 */
	private Comparator<T> getComparator(final String sortField, final Sort sort) {
		return new Comparator<T>() {

			@Override
			public int compare(T e1, T e2) {
				try {
					// 反射需要排序的字段
					Field field = e1.getClass().getDeclaredField(sortField);
					field.setAccessible(true);
					Object o1 = field.get(e1);
					Object o2 = field.get(e2);
					if (o1 == null && o2 == null)
						return 0;
					else if (o1 == null)
						return 1;
					else if (o2 == null)
						return -1;
					int rank = SortUtils.compare(o1, o2);
					switch (sort) {
						case ASC:
							return rank;
						case DESC:
							return -rank;
						default:
							return 0;
					}
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				return 0;
			}
		};
	}

	/**
	 * <pre>
	 * 排序
	 * 将集合按类中的某字段进行排序
	 * 如果参数类型为int型，则转换成int进行比较
	 * 否则toString()，然后调用String的compareTo()方法进行比较
	 * 如果集合中有null成员，则无论升降排序，其都将排至集合尾部
	 * </pre>
	 * @param list
	 *        排序集合
	 * @param sortField
	 *        排序字段
	 * @param sort
	 *        排序方式
	 */
	public void sort(List<T> list, String sortField, Sort sort) {
		Collections.sort(list, getComparator(sortField, sort));
	}
}
