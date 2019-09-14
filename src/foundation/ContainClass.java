package foundation;

/**
 * 使用内部类实现迭代器模式
 * @author Administrator
 *
 */
public class ContainClass {
	private Object[] items;
	private int index = 0;
	
	public ContainClass(int size) {
		items = new Object[size];
	}
	
	public void add(Object o){
		if(index < items.length){
			items[index++] = o;  //先获取index值之后才进行++操作
		}
	}
	
	/**
	 * 使用内部类实现迭代器接口
	 * @author Administrator
	 *
	 */
	private class InnerClass implements Selector {
		private int i = 0;
		@Override
		public boolean end() {
			return i == items.length;
		}
		@Override
		public Object current() {
			return items[i];
		}
		@Override
		public void next() {
			if(i < items.length){
				i++;
			}
		}
	}
	
	/**
	 * 调用实现了迭代器接口的内部类
	 * @return Selector
	 */
	public Selector getSelector(){
		return new InnerClass();
	}
	
	public static void main(String[] args) {
		ContainClass oc = new ContainClass(20);
		for (int i = 0; i < 20; i++) {
			oc.add(Integer.toString(i+1));
		}
		
		/**
		 * 使用iterator接口进行遍历查询
		 */
		Selector se = oc.getSelector();
		while(!se.end()){
			System.out.println("current:"+ se.current());
			se.next();
		}
	}

}

/**
 * 迭代器接口
 * @author Administrator
 *
 */
interface Selector{
	boolean end();
	Object current();
	void next();
}
