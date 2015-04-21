package application;

public class MousePosition
{
	private double mStartX , mStartY, mEndX, mEndY, oX, oY;
	public boolean start;
	
	// Constructor
	MousePosition()
	{
		mStartX = mStartY = mEndX = mEndY = oX = oY = 0.0;
		start = true;
	}
	
	public void setStartX(double value)
	{
		mStartX = value;
	}

	public void setStartY(double value)
	{
		mStartY = value;
	}

	public void setEndX(double value)
	{
		mEndX = value;
	}
	
	public void setEndY(double value)
	{
		mEndY = value;
	}
	
	public double getStartX()
	{
		return mStartX;
	}
	
	public double getStartY()
	{
		return mStartY;
	}
	
	public double getEndX()
	{
		return mEndX;
	}
	
	public double getEndY()
	{
		return mEndY;
	}
	
	public double getDifX()
	{
		return (getEndX() - getStartX());
	}
	
	public double getDifY()
	{
		return (getEndY() - getStartY());
	}
	
	public void calcOff()
	{
		calcOffX();
		calcOffY();
	}
	
	public void calcOffX()
	{
		oX += getDifX();
	}
	
	public void calcOffY()
	{
		oY += getDifY();
	}
	
	public double offX()
	{
		return oX;
	}
	
	public double offY()
	{
		return oY;
	}
	
	public void clear()
	{
		mStartX = mStartY = mEndX = mEndY = oX = oY = 0.0;
	}
}

