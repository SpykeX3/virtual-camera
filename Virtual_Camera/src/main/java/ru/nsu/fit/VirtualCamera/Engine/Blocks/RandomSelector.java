package ru.nsu.fit.VirtualCamera.Engine.Blocks;

import ru.nsu.fit.VirtualCamera.Engine.Frame;
import ru.nsu.fit.VirtualCamera.Engine.FunctionalBlock;

import java.util.LinkedList;
import java.util.List;

public class RandomSelector extends FunctionalBlock {

	List<Double> probability;

	public RandomSelector(List<String> args) throws IllegalArgumentException
	{
		validateArgs(args);
		int sum = 0;
		probability = new LinkedList<>();
		for (var x : args) sum += Integer.parseInt(x);

		int acc = 0;
		for (var x : args)
		{
			sum += Integer.parseInt(x);
			probability.add(acc * 1.0 / sum);
		}
	}

	@Override
	protected void validateArgs(List<String> args) throws IllegalArgumentException
	{
		for (var x : args)
		{
			Integer.parseInt(x);
		}
	}

	@Override
	public Frame performWork()
	{
		double val = Math.random();
		int id = 0;
		Frame frame = null;
		for (var x : probability)
		{
			if (x <= val)
			{
				frame = inputFrames.get(id);
			}
			id++;
		}
		return frame;
	}

	@Override
	protected void aftermath() {}

}
