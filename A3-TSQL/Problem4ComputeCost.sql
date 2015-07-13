create function computercost
(
  @pointid int,
  @x int,
  @y int,
  @theta decimal(18,6)
  )
returns decimal(18,6)
as

   begin
     declare @m int, -- the number of training examples
				@cost decimal(18,6); -- the cost value calculated by the function

		-- get the number of training examples in our set of data
		set @m = (select count(*) from  DATAPOINTS as dp inner join DATA as d on d.POINT_ID=dp.POINT_ID
	  where d.POINT_ID=@pointid );

		-- Calculate the cost
		set @cost = (1/(2 * @m)) * sum(square(convert(float, (@x * @theta - @y))));
					 

		return @cost;
	end