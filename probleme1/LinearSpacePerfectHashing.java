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

	/**
	 * Methode permettant de mettre à jour une nouvelle table de hashage
	 * @param array
	 */
	public void SetArray(ArrayList<AnyType> array)
	{
		AllocateMemory(array);
	}

	/**
	 * Methode permettant d'allouer la mémoire convenablement, et de placer l'array dans la table de hashage
	 * @param array
	 */
	@SuppressWarnings("unchecked")
	private void AllocateMemory(ArrayList<AnyType> array)
	{
		Random generator = new Random( System.nanoTime() );
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
			if (item != null)
				addElement(item);
	}

	/**
	 * Permet de calculer la taille de la table de hashage. Ne prend pas en compte les elements null
	 * @return
	 */
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

	/**
	 * Methode permet de voir s'il existe une valeur pour la clé donné en paramètre
	 * @param key
	 * @return
	 */
	public boolean containsKey(int key)
	{
		int position = (((a * key) + b) % p ) % data.length;
		if (data[position] == null)
			return false;
		else
			return data[position].containsKey(key);
	}
	
	/**
	 * Methode permettant d'obtenir la clé d'un element
	 * @param x
	 * @return
	 */
	public int getKey (AnyType x) {
		return (((a * x.hashCode()) + b) % p ) % data.length;	
	}
	
	/**
	 * Methode permettant de voir si la table de hashage contient un element x
	 * @param x
	 * @return
	 */
	public boolean containsValue (AnyType x) {
		int position = getKey(x);
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
	/**
	 * Methode permettant de supprimer un élément donné en paramètre.
	 * @param x
	 */
	public void remove (AnyType x) {
		int position = getKey(x);
		if (data[position] != null)
			data[position].remove(x);
	}

	/**
	 * Permet d'ajouter un element à la table de hashage
	 * @param item
	 */
	private void addElement(AnyType item)
	{
		int position = getKey(item);
		ArrayList<AnyType> listTemp = new ArrayList<AnyType>();
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
	
	
	
	/**
	 * affiche le contenu de la table de hashage
	 * return String
	 */
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
