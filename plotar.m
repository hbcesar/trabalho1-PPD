function plotar(){
	Array = csvread('1e.csv', ",");
	col1 = Array(:, 1);
	col2 = Array(:, 2);
	col3 = Array(:, 3);

	plot(col2, col3);
}