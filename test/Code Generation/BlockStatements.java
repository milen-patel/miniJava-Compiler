class TestBlockStmt {
	public static void main(String[] args) {
		int x = 0-0;
		{
			{
				{
					{
						{
							{
								{
									{
										{
											{
											 System.out.println(x); 
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		{
			int o = 1;
			int p = 1;
			int q = 1;
			int r = 1;
			int s = 1;
			System.out.println(234);	
		}
		{
			int a  = 2;
			{
				int b = 3;
				{
					int c = 4;
					{
						int d = 5;
						{
							int e = 6;
							System.out.println(x);
							System.out.println(a);
							System.out.println(b);
							System.out.println(c);
							System.out.println(d);
							System.out.println(e);
							System.out.println(e);
						}
						System.out.println(d);
					}
					System.out.println(c);
				}
				System.out.println(b);
			}
			System.out.println(a);
		}
		System.out.println(x);
	}
}
