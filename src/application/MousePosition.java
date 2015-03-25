package application;

public class MousePosition
{
	private double mStartX , mStartY, mEndX, mEndY;
	
	private int oX, oY;
	
	// Constructor
	MousePosition()
	{
		mStartX = mStartY = mEndX = mEndY = 0.0;
		oX = oY = 0;
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
		oX += (int) getDifX();
	}
	
	public void calcOffY()
	{
		oY += (int) getDifY();
	}
	
	public int offX()
	{
		return oX;
	}
	
	public int offY()
	{
		return oY;
	}
	
}
