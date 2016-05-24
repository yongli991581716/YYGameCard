package com.lordcard.common.task;

import java.util.Observable;
import java.util.Observer;

public class TaskManager extends Observable {

	private Observer observer;
	public static final Integer CANCEL_ALL = 1;

	public void cancelAll() {
		setChanged();
		notifyObservers(CANCEL_ALL);
		deleteObserver(observer);
		observer = null;
	}

	public void addTask(Observer observer) {
		this.observer = observer;
		super.addObserver(observer);
	}
}