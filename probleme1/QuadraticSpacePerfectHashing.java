package probleme1;

import java.util.ArrayList;
import java.util.Random;

public class QuadraticSpacePerfectHashing<AnyType> 
{
	static int p = 46337;

	private int a, b;
	private AnyType[] items;

	QuadraticSpacePerfectHashing()
	{
		a=b=0; items = null;
	}

	QuadraticSpacePerfectHashing(ArrayList<AnyType> array)
	{
		AllocateMemory(array);
	}

	public void SetArray(ArrayList<AnyType> array)
	{
		AllocateMemory(array);
	}

	public int Size()
	{
		if( items == null ) return 0;

		return items.length;
	}

	public boolean containsKey(int key)
	{
		return (items[((a * key + b) %p) % Size()] != null);
	}

	public boolean containsValue(AnyType x )
	{
		return (items[((a*x.hashCode() + b) %p) %(Size())] != null);
	}

	public void remove (AnyType x) {
		int currentPos = getKey(x);
		if (items[currentPos] != null)
			items[currentPos] = null;
	}

	public int getKey (AnyType x) {
		return ((a*x.hashCode() + b) %p) %(Size());
	}

	@SuppressWarnings("unchecked")
	private void AllocateMemory(ArrayList<AnyType> array)
	{
		Random generator = new Random( System.nanoTime() );

		if(array == null || array.size() == 0)
		{
			items = null;
			return;
		}
		else if(array.size() == 1)
		{
			a = b = 0;
			items = (AnyType[]) new Object[1];
			items[0] = array.get(0);
			return;
		}

		do
		{
			items = null;
			items = (AnyType[]) new Object[array.size() * array.size()];
			a = generator.nextInt(p);
			b = generator.nextInt(p);
			for (AnyType item : array)
			{
				if (item != null)
					items[getKey(item)] = item;
			}
		}
		while( collisionExists( array ) );
	}

	private boolean collisionExists(ArrayList<AnyType> array)
	{
		for (AnyType item : array)
		{
			if (items[getKey(item)] != item)
				return true;
		}
		return false;
	}
	
	public String toString () {
		String result = "";
		
		for (AnyType item : items)
		{
			if (item != null)
				result += "("+getKey(item) + ", "+item.toString() + "), ";
		}
		return result; 
	}
	public AnyType[] getItems()
	{
		return items;
	}
}
