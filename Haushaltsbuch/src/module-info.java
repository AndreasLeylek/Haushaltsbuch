
module MeinProjekt {
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.base;
	requires java.sql;
	requires java.desktop;
	exports view;
	exports model;
	opens view to javafx.base;
	
	
}