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

	/**
	 * Methode permettant de modifier l'array
	 * @param array
	 */
	public void SetArray(ArrayList<AnyType> array)
	{
		AllocateMemory(array);
	}

	/**
	 * Methode permettant de récupérer la taille du tableau. Ne prend pas en compte les valeurs null
	 * @return
	 */
	public int Size()
	{
		if( items == null ) return 0;

		return items.length;
	}

	/**
	 * Methode permettant de voir s'il existe une valeur pour la clé donné en paramètre
	 * @param key
	 * @return
	 */
	public boolean containsKey(int key)
	{
		return (items[((a * key + b) %p) % Size()] != null);
	}

	/**
	 * Methode permettant de voir si la valeur fourni en paramètre existe déjà en parametre
	 * @param x
	 * @return
	 */
	public boolean containsValue(AnyType x )
	{
		return (items[((a*x.hashCode() + b) %p) %(Size())] != null);
	}

	/**
	 * Methode permettant de supprimer un element de la table de hashage
	 * @param x
	 */
	public void remove (AnyType x) {
		int currentPos = getKey(x);
		if (items[currentPos] != null)
			items[currentPos] = null;
	}

	/**
	 * Methode permettant de recuperer la clé d'un element
	 * @param x
	 * @return
	 */
	public int getKey (AnyType x) {
		return ((a*x.hashCode() + b) %p) %(Size());
	}

	/**
	 * Methode permettant d'alloué la mémoire à items. Place les elements de array dans items.
	 * @param array
	 */
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

	/**
	 * Methode permettant de detecter s'il existe une collision entre les items et le tableau array passé en paramètre.
	 * @param array
	 * @return
	 */
	private boolean collisionExists(ArrayList<AnyType> array)
	{
		for (AnyType item : array)
		{
			if (items[getKey(item)] != item && items[getKey(item)] != null)
				return true;
		}
		return false;
	}
	
	/**
	 * Methode permettant de retourner un String representant la table de hashage.
	 * @return String
	 */
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
