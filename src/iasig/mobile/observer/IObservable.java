package iasig.mobile.observer;


public interface IObservable {
	
	//Ajout d'un observateur
	public void addObserver(IObserver observer);
	//Demander à l'observateur de mettre à jour
	public void updateObserver();
	//Supprimer les observateur
	public void delObserver();
	
}
