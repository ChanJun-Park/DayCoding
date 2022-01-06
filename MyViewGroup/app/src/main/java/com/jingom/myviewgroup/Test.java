package com.jingom.myviewgroup;

public class Test {
	private static class InnerTest {
		private int resId = 0;

		public InnerTest(int resId) {
			this.resId = resId;
		}

		public int getResId() {
			return resId;
		}

		public void setResId(int resId) {
			this.resId = resId;
		}
	}

	public void methodA() {
		InnerTest test = new InnerTest(1);
		int number = test.resId;
	}
}
