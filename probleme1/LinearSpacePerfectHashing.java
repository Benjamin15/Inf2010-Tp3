package probleme1;

import java.util.Random;
import java.util.ArrayList;

public class LinearSpacePerfectHashing<AnyType>
{
	static int p = 46337;

	QuadraticSpacePerfectHashing<AnyType>[] data;
	int a, b;

	LinearSpacePerfectHashing()
	{
		a=b=0; data = null;
	}

	LinearSpacePerfectHashing(ArrayList<AnyType> array)
	{
		AllocateMemory(array);
	}

	public void SetArray(ArrayList<AnyType> array)
	{
		AllocateMemory(array);
	}

	@SuppressWarnings("unchecked")
	private void AllocateMemory(ArrayList<AnyType> array)
	{
		Random generator = new Random( System.nanoTime() );
		ArrayList<AnyType> listTemp = new ArrayList<AnyType>();
		int position;
		
		if(array == null || array.size() == 0)
		{
			data = null;
			return;
		}
		if(array.size() == 1)
		{
			a = b = 0;
			data = (QuadraticSpacePerfectHashing<AnyType>[]) new Object[1];
			data[0] = (QuadraticSpacePerfectHashing<AnyType>) array.get(0);
			return;
		}

		data = new QuadraticSpacePerfectHashing[array.size()];
		a = generator.nextInt(p);
		b = generator.nextInt(p);

		for (AnyType item : array)
		{
			if (item != null)
			{
				position = (((a * item.hashCode()) + b) % p ) % data.length;
				if (data[position] == null) // Si la position de data est une case vide, on crée un hashage Quadratique
				{
					listTemp.add(item);
					data[position] = new QuadraticSpacePerfectHashing<AnyType>();
					data[position].SetArray(listTemp);
				}
				else
				{
					for (AnyType itemQuadratic : data[position].getItems()) // On met dans un arrayList les anciens element quadratique
					{
						if (itemQuadratic != null)
							listTemp.add(itemQuadratic);
					}
					listTemp.add(item); // On rajoute le nouvel element
					data[position].SetArray(listTemp);
				}
			}
		}
	}

	public int Size()
	{
		if( data == null ) return 0;

		int size = 0;
		for(int i=0; i<data.length; ++i)
		{
			size += (data[i] == null ? 1 : data[i].Size());
		}
		return size;
	}

	public boolean containsKey(int key)
	{
		int position = (((a * key) + b) % p ) % data.length;
		if (data[position] == null)
			return false;
		else
			return data[position].containsKey(key);
	}
	
	public int getKey (AnyType x) {
		return x.hashCode();	
	}
	
	public boolean containsValue (AnyType x) {
		int position = (((a * x.hashCode()) + b) % p ) % data.length;
		if (data[position] == null)
			return false;
		for (AnyType item : data[position].getItems())
		{
			if (item != null)
				if (item.equals(x))
					return true;
		}
		return false;
	}
	
	public void remove (AnyType x) {
		int position = (((a * x.hashCode()) + b) % p ) % data.length;
		if (data[position] != null)
			data[position].remove(x);
	}

	public String toString () {
		String result = "";
		for (QuadraticSpacePerfectHashing<AnyType> item : data)
		{
			if (item != null)
			{
				for (AnyType itemQuadratic : item.getItems())
				{
					if (itemQuadratic != null)
						result += itemQuadratic.toString();
				}
			}
		}
		return result; 
	}
	
}
