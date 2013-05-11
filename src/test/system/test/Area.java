package test.system.test;


public class Area {
	private Point startPoint;
	private Point endPoint;

	public Area() {
		startPoint = new Point();
		endPoint = new Point();
	}

	public Area(int startPointX, int startPointY, int width, int height) {
		this(new Point(startPointX, startPointY),
				new Point(startPointX + width, startPointY + height));
	}

	public Area(Point startPoint, Point endPoint) {
		this.startPoint = new Point(startPoint);
		this.endPoint = new Point(endPoint);
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public boolean hasPoint(int x,int y){
		return hasPoint(new Point(x, y));
	}
	
	public boolean hasPoint(Point point) {
		return hasPointInX(point.getX()) && hasPointInY(point.getY());
	}
	
	public boolean hasPointInX(int x){
		return startPoint.getX() <= x && x <= endPoint.getX();
	}
	public boolean hasPointInY(double y){
		return startPoint.getY() <= y && y <= endPoint.getY();
	}
	
	public int getWidth(){
		return endPoint.getX() - startPoint.getX();
	}
	public int getHeight(){
		return endPoint.getY() - startPoint.getY();
	}
	
}
