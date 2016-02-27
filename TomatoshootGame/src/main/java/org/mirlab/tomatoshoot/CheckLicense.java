package org.mirlab.tomatoshoot;

import net.emome.hamiapps.sdk.ForwardActivity;

public class CheckLicense extends ForwardActivity
{
	@SuppressWarnings("unchecked")
	@Override
	public Class getTargetActivity() 
	{
		return TomatoshootGame.class;
	}
	
	@Override
	public boolean passOnUnavailableDataNetwork()
	{
		return true;
	}
}
