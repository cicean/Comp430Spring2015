
if (object_id(N'regressionLear') is not null)
    drop procedure regressionLearn
go

create procedure regressionLearn

as

begin
    
	declare @coef table (Dim int, Theta decimal(18,6)) 
    declare @prictim table(Point_id int,Thetax decimal(18,6),PRIMARY KEY (Point_id))
	
	declare @pointid int,
	        @dim int;

	set @pointid =(select POINT_ID from  DATAPOINTS where ISTRAINING =1);
	set @dim = (select DISTINCT DIM  from DATA where POINT_ID= @pointid);

    declare @regularizationParam decimal(18,6),--lamba
	         @stoppingCriteria decimal(18,6), --
             @learningRate decimal(18,6); --alpha
	set  @regularizationParam=1;
	set    @learningRate =0.01;
	set  @stoppingCriteria=0.5;

    declare @theta decimal(18,6);
	declare @m int;
	declare @temp decimal(18,6);

	set @theta = 0;      
	set @m= (select count(*) from  DATAPOINTS where ISTRAINING =1);
	set @temp=(select sum(VALUE*cf.Theta) as temp
		from data as d inner join @coef as cf on cf.dim = d.DIM
		where POINT_ID=@pointid
		group by POINT_ID )

   insert @coef values(@dim,@theta) 
   insert  @prictim values(@pointid,@temp)

   declare @iterations int;
   declare @cost decimal(18,6);
   declare @costhistory decimal(18,6);    
		
   set @iterations=1;
   select @cost = @costhistory  + square(CLASSLABEL-ver.Thetax)/(2*@m)
		from(select  p.Thetax,CLASSLABEL
		from @prictim as p  inner join datapoints as dp on dp.POINT_ID=p.POINT_ID
		where p.POINT_ID=@pointid ) as ver

		if ((@cost-@costhistory)/@costhistory<@stoppingCriteria)
		begin
		select @theta = @theta - @learningRate*(ver2.CLASSLABEL-temp)*VALUE/@m
		from(
		select Value, sum(VALUE*cf2.Theta) as temp,dp.CLASSLABEL,cf2.Theta
		from data as d2 inner join @coef as cf2 on cf2.dim = d2.DIM inner join datapoints as dp on dp.POINT_ID=d2.POINT_ID
		where d2.POINT_ID=@pointid) as ver2
		group by ver2.DIM
		UPDATE @coef
        SET Theta =@theta
		Set @costhistory=@cost
		set @iterations=@iterations+1
		fetch numberitem into @pointid;
		end
		else
		begin
		
	    print 'stop Learning complete'
		print 'Final loss value was '+@cost+'after '+@iterations +'iterations'

		end

		
		
	   
   end
	  
    
end