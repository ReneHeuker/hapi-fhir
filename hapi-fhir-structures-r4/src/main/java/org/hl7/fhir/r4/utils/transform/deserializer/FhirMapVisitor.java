//
// Translated by Java2J (http://www.cs2j.com): 8/18/2017 3:07:36 PM
//

package org.hl7.fhir.r4.utils.transform.deserializer;



import com.sun.istack.internal.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.VisitorExtensions;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaBaseVisitor;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
* ANTLR Visitor class.
*/
public class FhirMapVisitor  extends FhirMapJavaBaseVisitor<Object>
{
    /**
     Maximum length of a name.
     */
    private static final int MaxNameLength = 64;

    /**
     Class to execute fhir map commands.
     */
    private IFhirMapExecutor executor;

  public IFhirMapExecutor getExecutor() {
    return executor;
  }
  public void setExecutor(IFhirMapExecutor executor){
    this.executor = executor;
  }

  /**
     Lazy create url processor.
     */
    private UrlProcessor getUrlProcessor() throws Exception {
        if (this.urlProcessor == null)
        {
            this.urlProcessor = new UrlProcessor();
        }
        return this.urlProcessor;
    }
    private UrlProcessor urlProcessor;

    /**
     Delegate for optional dumping of info.

     */
    @FunctionalInterface
    public interface DumpDelegate
    {
        void invoke(String msg);
    }

    /**
     Set this to callback function to dump parsing messages.
     */
    public DumpDelegate DumpFcn = null;

    /**
     Constructor.

     */
    public FhirMapVisitor(IFhirMapExecutor executor)
    {
        this.executor = executor;
    }

    /**
     Parse grammar rule keyMap.
     This will trigger a Executor.Map callback.

     @param context
     @return null
     */
    @Override
    public Object visitKeyMap(FhirMapJavaParser.KeyMapContext context) {
        UrlData urlData = null;
        try {
            urlData = (UrlData) this.visit(context.structureMap());
            String name = (String) this.visit(context.quotedString());
            this.executor.map(urlData, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     Parse grammar rule structureMap.

     @param context
     @return UrlData instance
     */
    @Override
    public Object visitStructureMap(FhirMapJavaParser.StructureMapContext context) {
            return (UrlData) this.visit(context.quotedUrl());

    }

    /**
     Parse grammar rule keyImports.
     This will trigger a Executor.Imports callback.

     @param context
     @return null
     */
    @Override
    public Object visitKeyImports(FhirMapJavaParser.KeyImportsContext context) {
        UrlData urlData = null;
            urlData = (UrlData) this.visit(context.structureMap());

        try {
            this.executor.imports(urlData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule structureDefinition.

     @param context
     @return UrlData instance
     */
    @Override
    public Object visitStructureDefinition(FhirMapJavaParser.StructureDefinitionContext context) {
            return this.visit(context.quotedUrl());
    }

    /**
     Parse grammar rule identifier
     This verifies that thwe identifier is not too long.

     @param context
     @return String identifier
     */
    @Override
    public Object visitIdentifier(FhirMapJavaParser.IdentifierContext context)
    {
        String retVal = context.getText(); // get string characters
        if (retVal.length() > MaxNameLength)
        {
            throw new RuntimeException("Identifier must be less than {MaxNameLength} characters.  '{retVal}'");
        }
        return retVal;
    }

    /**
     Parse grammar rule quotedIdentifier

     @param context
     @return String without the surrounding quotes
     */
    @Override
    public Object visitQuotedIdentifier(FhirMapJavaParser.QuotedIdentifierContext context)
    {
        String retVal = context.getText(); // get string characters
        retVal = retVal.substring(1, 1 + retVal.length() - 2); // remove surrounding double quotes.
        return retVal;
    }

    /**
     Parse grammar rule quotedString

     @param context
     @return String without the surrounding quotes
     */
    @Override
    public Object visitQuotedString(FhirMapJavaParser.QuotedStringContext context)
    {
        String retVal = context.getText(); // get string characters
        retVal = retVal.substring(1, 1 + retVal.length() - 2); // remove surrounding double quotes.
        return retVal;
    }

    /**
     Parse grammar rule quotedStringWQuotes

     @param context
     @return String without the surrounding quotes
     */
    @Override
    public Object visitQuotedStringWQuotes(FhirMapJavaParser.QuotedStringWQuotesContext context)
    {
        String retVal = context.getText(); // get string characters
        return retVal;
    }

    /**
     Parse grammar rule int
     created.

     @param context
     @return Int32 value
     */
    @Override
    public Object visitInteger(FhirMapJavaParser.IntegerContext context)
    {
        return Integer.parseInt(context.getText());
    }

    /**
     Parse grammar rule quotedUrl
     The url parser is split off from this because of some incompatabilitied between the two
     grammars. Here we pass the url portion to this seperate parser and return the UrlData
     created.

     @param context
     @return UrlData instance
     */
    @Override
    public Object visitQuotedUrl(@NotNull FhirMapJavaParser.QuotedUrlContext context) {
        String urlStr = null;

            urlStr = (String) this.visit(context.quotedString());

      try {
        return this.getUrlProcessor().parseUrl(urlStr);
      } catch (Exception e) {
        e.printStackTrace();
      }

      return null;
    }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region Uses methods
    /**
     Parse grammar rule keyUses.
     This will trigger a Executor.Uses callback.

     @param context
     @return null
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUses([NotNull] KeyUsesContext context)
    @Override
    public Object visitKeyUses(@NotNull FhirMapJavaParser.KeyUsesContext context) {
        UrlData urlData = null;
            urlData = (UrlData) this.visit(context.structureDefinition());

        FhirMapUseNames name = (FhirMapUseNames) this.visit(context.keyUsesName());
        try {
            this.executor.uses(urlData, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule keyUsesName.

     @param context
     @return null
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesName([NotNull] KeyUsesNameContext context)
    @Override
    public Object visitKeyUsesName(@NotNull FhirMapJavaParser.KeyUsesNameContext context)
    {
        return this.visitChildren(context);
    }

    /**
     Parse grammar rule keyUsesNameSource.

     @param context
     @return UseNames.Source
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesNameSource([NotNull] KeyUsesNameSourceContext context)
    @Override
    public Object visitKeyUsesNameSource(@NotNull FhirMapJavaParser.KeyUsesNameSourceContext context)
    {
        return FhirMapUseNames.Source;
    }

    /**
     Parse grammar rule keyUsesNameTarget.

     @param context
     @return UseNames.Target
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesNameTarget([NotNull] KeyUsesNameTargetContext context)
    @Override
    public Object visitKeyUsesNameTarget(@NotNull FhirMapJavaParser.KeyUsesNameTargetContext context)
    {
        return FhirMapUseNames.Target;
    }

    /**
     Parse grammar rule keyUsesNameQueried.

     @param context
     @return UseNames.Queried
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesNameQueried([NotNull] KeyUsesNameQueriedContext context)
    @Override
    public Object visitKeyUsesNameQueried(@NotNull FhirMapJavaParser.KeyUsesNameQueriedContext context)
    {
        return FhirMapUseNames.Queried;
    }

    /**
     Parse grammar rule keyUsesNameProduced.

     @param context
     @return UseNames.Produced
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitKeyUsesNameProduced([NotNull] KeyUsesNameProducedContext context)
    @Override
    public Object visitKeyUsesNameProduced(FhirMapJavaParser.KeyUsesNameProducedContext context)
    {
        return FhirMapUseNames.Produced;
    }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region Group Methods
    /**
     Parse grammar rule groupStart.

     @param context
     @return GroupTypes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupStart([NotNull] GroupStartContext context)
    @Override
    public Object visitGroupStart(@NotNull FhirMapJavaParser.GroupStartContext context)  {
        try { //TODO: MAKE SURE THE NULLABLE FIELDS CAN BE NULL!
          FhirMapJavaParser.GroupExtendsContext extendsContext = context.groupExtends();
          String extension;
          if (context.groupExtends() != null){
            extension = (String) this.visit(extendsContext);
          }
          else {
            extension = null;
          }
            this.executor.groupStart( (String) this.visit(context.identifier()), (FhirMapGroupTypes) this.visit(context.groupType()), extension);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule groupEnd.

     @param context
     @return GroupTypes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupEnd([NotNull] GroupEndContext context)
    @Override
    public Object visitGroupEnd(@NotNull FhirMapJavaParser.GroupEndContext context) {
        try {
            this.executor.groupEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule groupExtends.

     @param context
     @return List<String> of group
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupExtends([NotNull] GroupExtendsContext context)
    @Override
    public Object visitGroupExtends(@NotNull FhirMapJavaParser.GroupExtendsContext context) {
            return this.visit(context.identifier());
    }

    /**
     Parse grammar rule groupType.

     @param context
     @return GroupTypes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupType([NotNull] GroupTypeContext context)
    @Override
    public Object visitGroupType(FhirMapJavaParser.GroupTypeContext context)
    {
        return this.visitChildren(context);
    }

    /**
     Parse grammar rule groupTypeType.

     @param context
     @return GroupTypes.Type
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupTypeType([NotNull] GroupTypeTypeContext context)
    @Override
    public Object visitGroupTypeType(@NotNull FhirMapJavaParser.GroupTypeTypeContext context)
    {
        return FhirMapGroupTypes.Types;
    }

    /**
     Parse grammar rule groupTypeTypeTypes.

     @param context
     @return GroupTypes.TypeTypes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitGroupTypeTypeTypes([NotNull] GroupTypeTypeTypesContext context)
    @Override
    public Object visitGroupTypeTypeTypes(FhirMapJavaParser.GroupTypeTypeTypesContext context)
    {
        return FhirMapGroupTypes.TypeTypes;
    }

    /**
     Parse grammar rule ruleInput.

     @param context
     @return FhirMapRuleInput
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInput([NotNull] RuleInputContext context)
    @Override
    public Object visitRuleInput(@NotNull FhirMapJavaParser.RuleInputContext context)  {
        try {
            this.executor.groupInput( (String) this.visit(context.ruleInputName()), (String) this.visit(context.ruleInputType()), (FhirMapInputModes) this.visit(context.ruleInputMode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleInputModes.

     @param context
     @return FhirMapInputModes
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInputMode([NotNull] RuleInputModeContext context)
    @Override
    public Object visitRuleInputMode(@NotNull FhirMapJavaParser.RuleInputModeContext context)  {
            return this.visit(context.ruleInputModes());
    }

    /**
     Parse grammar rule ruleInputModesSource.

     @param context
     @return FhirMapInputModes.Source
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInputModesSource([NotNull] RuleInputModesSourceContext context)
    @Override
    public Object visitRuleInputModesSource(@NotNull FhirMapJavaParser.RuleInputModesSourceContext context)
    {
        return FhirMapInputModes.Source;
    }

    /**
     Parse grammar rule ruleInputModesTarget.

     @param context
     @return FhirMapInputModes.Target
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInputModesTarget([NotNull] RuleInputModesTargetContext context)
    @Override
    public Object visitRuleInputModesTarget(FhirMapJavaParser.RuleInputModesTargetContext context)
    {
        return FhirMapInputModes.Target;
    }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region Rule Methods

    /**
     Parse grammar rule ruleInstance

     @param context
     @return null
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleInstance([NotNull] RuleInstanceContext context)
    @Override
    public Object visitRuleInstance(@NotNull FhirMapJavaParser.RuleInstanceContext context) {
        try {
            this.executor.ruleStart((List<String>) this.visit(context.ruleName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.visitChildren(context);
        try {
            this.executor.ruleComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetReference

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetReference([NotNull] RuleTargetReferenceContext context)
    @Override
    public Object visitRuleTargetReference(@NotNull FhirMapJavaParser.RuleTargetReferenceContext context) {
        try {
            List<String> ctx = null;
            String refSource = null;
            String targetVar = null;
            if (context.ruleTargetContext() != null){
              ctx = (List<String>) this.visit(context.ruleTargetContext());
            }
          if (context.ruleTargetReferenceSource() != null){
            refSource = (String) this.visit(context.ruleTargetReferenceSource());
          }
          if (context.ruleTargetVariable() != null){
            targetVar = (String) this.visit(context.ruleTargetVariable());
          }

            this.executor.transformReference(ctx,
              refSource,
              targetVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetTruncate

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTruncate([NotNull] RuleTargetTruncateContext context)
    @Override
    public Object visitRuleTargetTruncate(@NotNull FhirMapJavaParser.RuleTargetTruncateContext context) {
        try {
            //Not implented in fhir code
            this.executor.transformTruncate((List<String>) this.visit(context.ruleTargetContext()), (String)  this.visit(context.ruleTargetTruncateSource()), (Integer) this.visit(context.ruleTargetTruncateLength()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetCast

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCast([NotNull] RuleTargetCastContext context)
    @Override
    public Object visitRuleTargetCast(@NotNull FhirMapJavaParser.RuleTargetCastContext context) {
        try {
            this.executor.transformCast((List<String>) this.visit(context.ruleTargetContext()), (String) this.visit(context.ruleTargetCastSource()), (String) this.visit(context.ruleTargetCastType()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetAs

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetAs([NotNull] RuleTargetAsContext context)
    @Override
    public Object visitRuleTargetAs(@NotNull FhirMapJavaParser.RuleTargetAsContext context) {
        try {
            this.executor.transformAs((List<String>) this.visit(context.ruleContext()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetAssign

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetAssign([NotNull] RuleTargetAssignContext context)
    @Override
    public Object visitRuleTargetAssign(@NotNull FhirMapJavaParser.RuleTargetAssignContext context) {
        try {
          List<String> ctx = null;
          String assignVal = null;
          String targetVar = null;
          if (context.ruleTargetContext()!=null){
            ctx = (List<String>) (this.visit(context.ruleTargetContext()));
          }
          if (context.ruleTargetAssignValue()!=null){
            assignVal = (String) (this.visit(context.ruleTargetAssignValue()));
          }
          if (context.ruleTargetVariable()!=null){
            targetVar = (String) (this.visit(context.ruleTargetVariable()));
          }
            this.executor.transformCopy(ctx, assignVal, targetVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetCopy

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCopy([NotNull] RuleTargetCopyContext context)
    @Override
    public Object visitRuleTargetCopy(@NotNull FhirMapJavaParser.RuleTargetCopyContext context) {
        try {

            this.executor.transformCopy((List<String>) this.visit(context.ruleTargetContext()), (String) this.visit(context.ruleTargetCopySource()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetCreate

     @param context
     @return List<String>
     */
    @Override
    public Object visitRuleTargetCreate(FhirMapJavaParser.RuleTargetCreateContext context) {
        try {
            List<String> ctx = null;
            String createType = null;
            String ruleTarget = null;
            if (context.ruleTargetContext() != null){
              ctx = (List<String>) this.visit(context.ruleTargetContext());
            }
          if (context.ruleTargetCreateType() != null){
            createType = (String) this.visit(context.ruleTargetCreateType());
          }
          if (context.ruleTargetVariable() != null){
            ruleTarget = (String) this.visit(context.ruleTargetVariable());
          }
            this.executor.transformCreate(ctx,createType,ruleTarget);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetTranslate

     @param context
     @return List<String>
     */
    @Override
    public Object visitRuleTargetTranslate(FhirMapJavaParser.RuleTargetTranslateContext context) {
        try {
          List<String> ctx = null;
          String source = null;
          UrlData map = null;
          FhirMapTranslateOutputTypes types = null;
          String targetVar = null;

          if (context.ruleTargetContext() != null){
            ctx = (List<String>) this.visit(context.ruleTargetContext());
          }
          if (context.ruleTargetTranslateSource() != null){
            source = (String) this.visit(context.ruleTargetTranslateSource());
          }
          if (context.ruleTargetTranslateMap() != null){
            map = (UrlData) this.visit(context.ruleTargetTranslateMap());
          }
          if (context.ruleTargetTranslateOutput() != null){
            types = (FhirMapTranslateOutputTypes) this.visit(context.ruleTargetTranslateOutput());
          }
          if (context.ruleTargetVariable() != null){
            targetVar = (String) this.visit(context.ruleTargetVariable());
          }


          this.executor.transformTranslate(ctx, source, map, types, targetVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetTranslateOutputCode

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputCode([NotNull] RuleTargetTranslateOutputCodeContext context)
    @Override
    public Object visitRuleTargetTranslateOutputCode(@NotNull FhirMapJavaParser.RuleTargetTranslateOutputCodeContext context)
    {
        return FhirMapTranslateOutputTypes.Code;
    }

    /**
     Parse grammar rule ruleTargetTranslateOutputSystem

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputSystem([NotNull] RuleTargetTranslateOutputSystemContext context)
    @Override
    public Object visitRuleTargetTranslateOutputSystem(@NotNull FhirMapJavaParser.RuleTargetTranslateOutputSystemContext context)
    {
        return FhirMapTranslateOutputTypes.System;
    }

    /**
     Parse grammar rule ruleTargetTranslateOutputDisplay

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputDisplay([NotNull] RuleTargetTranslateOutputDisplayContext context)
    @Override
    public Object visitRuleTargetTranslateOutputDisplay(@NotNull FhirMapJavaParser.RuleTargetTranslateOutputDisplayContext context)
    {
        return FhirMapTranslateOutputTypes.Display;
    }

    /**
     Parse grammar rule ruleTargetTranslateOutputCoding

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputCoding([NotNull] RuleTargetTranslateOutputCodingContext context)
    @Override
    public Object visitRuleTargetTranslateOutputCoding(@NotNull FhirMapJavaParser.RuleTargetTranslateOutputCodingContext context)
    {
        return FhirMapTranslateOutputTypes.Coding;
    }

    /**
     Parse grammar rule ruleTargetTranslateOutputCodeableConcept

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetTranslateOutputCodeableConcept([NotNull] RuleTargetTranslateOutputCodeableConceptContext context)
    @Override
    public Object visitRuleTargetTranslateOutputCodeableConcept(@NotNull FhirMapJavaParser.RuleTargetTranslateOutputCodeableConceptContext context)
    {
        return FhirMapTranslateOutputTypes.CodeableConcept;
    }

    /**
     Parse grammar rule ruleTargetCp

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCp([NotNull] RuleTargetCpContext context)
    @Override
    public Object visitRuleTargetCp(@NotNull FhirMapJavaParser.RuleTargetCpContext context) {
        try {
            this.executor.transformCp((List<String>) this.visit(context.ruleTargetContext()), (UrlData) this.visit(context.ruleTargetCpSystem()), (String) this.visit(context.ruleTargetCpVariable()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetAppend

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetAppend([NotNull] RuleTargetAppendContext context)
    @Override
    public Object visitRuleTargetAppend(@NotNull FhirMapJavaParser.RuleTargetAppendContext context) {
        try {
          //Not implemented
            this.executor.transformAppend((List<String>) this.visit(context.ruleTargetContext()), (List<String>) this.visit(context.ruleTargetAppendSources()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetAppendSources

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetAppendSources([NotNull] RuleTargetAppendSourcesContext context)
    @Override
    public Object visitRuleTargetAppendSources(@NotNull FhirMapJavaParser.RuleTargetAppendSourcesContext context)
    {
      ArrayList<String> values = new ArrayList<String>();
      String[] retVals = new String[context.ruleTargetAppendSource().size()];
      if (context.ruleTargetAppendSource() != null){
        int count = context.ruleTargetAppendSource().size();
        for (ParseTree treeItem : context.ruleTargetAppendSource()){
          values.add((String) this.visit(treeItem));
        }
      }
      return values;
        //return VisitorExtensions.VisitMultiple(this, context.ruleTargetAppendSource(), values);
    }

    /**
     Parse grammar rule ruleTargetC

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetC([NotNull] RuleTargetCContext context)
    @Override
    public Object visitRuleTargetC(@NotNull FhirMapJavaParser.RuleTargetCContext context) {
        try {
          List<String> ctx = null;
          UrlData system = null;
          String code = null;
          String display = null;
          String targetVar = null;

          if (context.ruleTargetContext() != null){
            ctx = (List<String>) this.visit(context.ruleTargetContext());
          }
          if (context.ruleTargetCSystem() != null){
            system = (UrlData) this.visit(context.ruleTargetCSystem());
          }
          if (context.ruleTargetCCode() != null){
            code = (String) this.visit(context.ruleTargetCCode());
          }
          if (context.ruleTargetCDisplay() != null){
            display = (String) this.visit(context.ruleTargetCDisplay());
          }
          if (context.ruleTargetVariable() != null){
            targetVar = (String) this.visit(context.ruleTargetVariable());
          }
            this.executor.transformCoding(ctx, system, code, display, targetVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetCC1

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCC1([NotNull] RuleTargetCC1Context context)
    @Override
    public Object visitRuleTargetCC1(@NotNull FhirMapJavaParser.RuleTargetCC1Context context) {
        try {
            this.executor.transformCodeableConcept((List<String>) this.visit(context.ruleTargetContext()),(String)  this.visit(context.ruleTargetCC1Text()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetCC2

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetCC2([NotNull] RuleTargetCC2Context context)
    @Override
    public Object visitRuleTargetCC2(@NotNull FhirMapJavaParser.RuleTargetCC2Context context) {
        try {
          List<String> ctx = null;
          UrlData system = null;
          String code = null;
          String display = null;
          String var = null;
          if (context.ruleTargetContext() != null){
            ctx = (List<String>) this.visit(context.ruleTargetContext());
          }
          if (context.ruleTargetCC2System() != null){
            system = (UrlData) this.visit(context.ruleTargetCC2System());
          }
          if (context.ruleTargetCC2Code() != null){
            code = (String) this.visit(context.ruleTargetCC2Code());
          }
          if (context.ruleTargetCC2Display() != null){
            display = (String) this.visit(context.ruleTargetCC2Display());
          }
          if (context.ruleTargetVariable() != null){
            var = (String) this.visit(context.ruleTargetVariable());
          }
            this.executor.transformCodeableConcept(ctx, system, code, display, var);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetContext

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetContext([NotNull] RuleTargetContextContext context)
    @Override
    public Object visitRuleTargetContext(@NotNull FhirMapJavaParser.RuleTargetContextContext context) {
            return this.visit(context.ruleContext());
    }

    /**
     Parse grammar rule ruleTargetVariable

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetVariable([NotNull] RuleTargetVariableContext context)
    @Override
    public Object visitRuleTargetVariable(@NotNull FhirMapJavaParser.RuleTargetVariableContext context) {
            return this.visit(context.identifier());
    }

    /**
     Parse grammar rule ruleTargetEscape

     @param context
     @return List<String>
     */
    @Override
    public Object visitRuleTargetEscape(FhirMapJavaParser.RuleTargetEscapeContext context)
    {
        try {
          List<String> ctx = null;
          String var = null;
          String str1 = null;
          String str2 = null;
          String targetVar = null;
          if (context.ruleTargetContext() != null){
            ctx = (List<String>) this.visit(context.ruleTargetContext());
          }
          if (context.ruleTargetEscapeVariable() != null){
            var = (String) this.visit(context.ruleTargetEscapeVariable());
          }
          if (context.ruleTargetEscapeString1() != null){
            str1 = (String) this.visit(context.ruleTargetEscapeString1());
          }
          if (context.ruleTargetEscapeString2() != null){
            str2 = (String) this.visit(context.ruleTargetEscapeString2());
          }
          if (context.ruleTargetVariable() != null){
            targetVar = (String) this.visit(context.ruleTargetVariable());
          }

            this.executor.transformEscape(ctx, var, str1, str2, targetVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetEvaluate

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetEvaluate([NotNull] RuleTargetEvaluateContext context)
    @Override
    public Object visitRuleTargetEvaluate(@NotNull FhirMapJavaParser.RuleTargetEvaluateContext context) {
        try {
          List<String> ctx = null;
          String obj = null;
          String element = null;
          String targetVar = null;
          if (context.ruleTargetContext() != null){
            ctx = (List<String>) this.visit(context.ruleTargetContext());
          }
          if (context.ruleTargetEvaluateObject() != null){
            obj = (String) this.visit(context.ruleTargetEvaluateObject());
          }
          if (context.ruleTargetEvaluateObjectElement() != null){
            element = (String) this.visit(context.ruleTargetEvaluateObjectElement());
          }
          if (context.ruleTargetVariable() != null){
            targetVar = (String) this.visit(context.ruleTargetVariable());
          }
            this.executor.transformEvaluate(ctx, obj, element, targetVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetId

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetId([NotNull] RuleTargetIdContext context)
    @Override
    public Object visitRuleTargetId(@NotNull FhirMapJavaParser.RuleTargetIdContext context) {
        try {
            this.executor.transformId((List<String>) this.visit(context.ruleTargetContext()), (UrlData) this.visit(context.ruleTargetIdSystem()),  (String) this.visit(context.ruleTargetIdValue()), (String) this.visit(context.ruleTargetIdType()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetPointer

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetPointer([NotNull] RuleTargetPointerContext context)
    @Override
    public Object visitRuleTargetPointer(@NotNull FhirMapJavaParser.RuleTargetPointerContext context) {
        try {
          //Not Implemented
            this.executor.transformPointer((List<String>) this.visit(context.ruleTargetContext()), (String) this.visit(context.ruleTargetPointerResource()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetQty1

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetQty1([NotNull] RuleTargetQty1Context context)
    @Override
    public Object visitRuleTargetQty1(@NotNull FhirMapJavaParser.RuleTargetQty1Context context) {
        try {
          List<String> ctx = null;
          String text = null;
          String targetVar = null;
          if (context.ruleTargetContext() != null){
            ctx = (List<String>) this.visit(context.ruleTargetContext());
          }
          if (context.ruleTargetContext() != null){
            text = (String) this.visit(context.ruleTargetQty1Text());
          }
          if (context.ruleTargetContext() != null){
            targetVar = (String) this.visit(context.ruleTargetVariable());
          }
            this.executor.transformQty(ctx,text,targetVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetQty2

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetQty2([NotNull] RuleTargetQty2Context context)
    @Override
    public Object visitRuleTargetQty2(@NotNull FhirMapJavaParser.RuleTargetQty2Context context) {
        try {
          List<String> ctx = null;
          String value = null;
          String unitSystem = null;
          UrlData system = null;
          String targetVar = null;
          if (context.ruleTargetContext() != null){
            ctx = (List<String>) this.visit(context.ruleTargetContext());
          }
          if (context.ruleTargetQty2Value() != null){
            value = (String) this.visit(context.ruleTargetQty2Value());
          }
          if (context.ruleTargetQty2UnitString() != null){
            unitSystem = (String) this.visit(context.ruleTargetQty2UnitString());
          }
          if (context.ruleTargetQty2System() != null){
            system = (UrlData) this.visit(context.ruleTargetQty2System());
          }
          if (context.ruleTargetVariable() != null){
            targetVar = (String) this.visit(context.ruleTargetVariable());
          }
          this.executor.transformQty(ctx, value, unitSystem, system, targetVar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetQty3

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetQty3([NotNull] RuleTargetQty3Context context)
    @Override
    public Object visitRuleTargetQty3(@NotNull FhirMapJavaParser.RuleTargetQty3Context context) {
        try {
          List<String> ctx = null;
          String value = null;
          String unitString = null;
          String codeVar = null;
          String targetVar = null;
          if (context.ruleTargetContext() != null){
            ctx = (List<String>) this.visit(context.ruleTargetContext());
          }
            this.executor.transformQty((List<String>) this.visit(context.ruleTargetContext()),
              (String) this.visit(context.ruleTargetQty3Value()),
              (String) this.visit(context.ruleTargetQty3UnitString()),
              (String) this.visit(context.ruleTargetQty3CodeVariable()),
              (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetUuid

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetUuid([NotNull] RuleTargetUuidContext context)
    @Override
    public Object visitRuleTargetUuid(@NotNull FhirMapJavaParser.RuleTargetUuidContext context) {
        try {
          //not implemented
            this.executor.transformUuid((List<String>) this.visit( context.ruleTargetContext()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleTargetEscape

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleTargetDateOp([NotNull] RuleTargetDateOpContext context)
    @Override
    public Object visitRuleTargetDateOp(@NotNull FhirMapJavaParser.RuleTargetDateOpContext context) {
        try {
          //not implemented
            this.executor.transformDateOp((List<String>) this.visit(context.ruleTargetContext()),(String)  this.visit(context.ruleTargetDateOpVariable()), (String) this.visit(context.ruleTargetDateOpOperation()), (String) this.visit(context.ruleTargetDateOpVariable2()), (String) this.visit(context.ruleTargetVariable()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleName

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleName([NotNull] RuleNameContext context)
    @Override
    public Object visitRuleName(@NotNull FhirMapJavaParser.RuleNameContext context)
    {
        ArrayList<String> values = new ArrayList<String>();
        String[] retVals = new String[context.identifier().size()];
        if (context.identifier() != null){
          int count = context.identifier().size();
          for (ParseTree treeItem : context.identifier()){
            values.add((String) this.visit(treeItem));
          }
        }
        return values;
        //return VisitorExtensions.VisitMultiple(this, context.identifier(), values);
    }

    /**
     Parse grammar rule ruleSource

     @param context
     @return FhirMapRuleType instance
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleSource([NotNull] RuleSourceContext context)
    @Override
    public Object visitRuleSource(@NotNull FhirMapJavaParser.RuleSourceContext context) {
        try {
          List<String> ctx = null;
          FhirMapJavaParser.RuleTypeContext typeContext = context.ruleType();
          FhirMapRuleType type;
          FhirMapJavaParser.RuleDefaultContext defaultContext = context.ruleDefault();
          String defaultVal = null;
          FhirMapJavaParser.RuleListOptionContext listOptionContext = context.ruleListOption();
          FhirMapListOptions listOptions = null;
          FhirMapJavaParser.RuleVariableContext variableContext = context.ruleVariable();
          String var = null;
          FhirMapJavaParser.RuleWherePathContext wherePathContext = context.ruleWherePath();
          String where = null;
          FhirMapJavaParser.RuleCheckPathContext checkPathContext = context.ruleCheckPath();
          String check = null;
          ctx = (List<String>) this.visit(context.ruleContext());
          if (typeContext != null){
            type = (FhirMapRuleType) this.visit(typeContext);
          }
          else {
            type = null;
          }
          if (defaultContext != null ){
            defaultVal = (String) this.visit(defaultContext);
          }
          if (listOptionContext != null){
            listOptions = (FhirMapListOptions) this.visit(listOptionContext);
          }
          if (variableContext != null){
            var = (String) this.visit(variableContext);
          }
          if (wherePathContext != null){
            where = (String) this.visit(wherePathContext);
          }
          if (checkPathContext != null){
            check = (String) this.visit(checkPathContext);
          }
            this.executor.ruleSource(ctx,
              type,
              defaultVal,
              listOptions,
              var,
              where,
              check
              );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleType

     @param context
     @return FhirMapRuleType instance
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleType([NotNull] RuleTypeContext context)
    @Override
    public Object visitRuleType(@NotNull FhirMapJavaParser.RuleTypeContext context) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        FhirMapRuleType tempVar = new FhirMapRuleType();
        tempVar.TypeName = (String) this.visit(context.identifier());
        for (ParseTree treeItem : context.integer())
            {
                List<Integer> occurances;
              if (tempVar.Occurances == null) {
                occurances = new ArrayList<>();
              }
              else {
                  occurances =tempVar.Occurances;

              }
              int i = (int) this.visit(treeItem);
                  occurances.add(i);
                tempVar.Occurances = occurances;
            }

        return tempVar;
    }

    /**
     Parse grammar rule ruleDefault
     #! Verify format of default value. Currently accepts an identifier.
     #! Also write test for this...

     @param context
     @return String
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleDefault([NotNull] RuleDefaultContext context)
    @Override
    public Object visitRuleDefault(@NotNull FhirMapJavaParser.RuleDefaultContext context)  {
        try {
            return VisitorExtensions.<String>VisitOrDefault(this, context.identifier(), String.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     Parse grammar rule ruleVariable

     @param context
     @return String
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleVariable([NotNull] RuleVariableContext context)
    @Override
    public Object visitRuleVariable(FhirMapJavaParser.RuleVariableContext context)  {
            return this.visit(context.identifier());
    }

    /**
     Parse grammar rule ruleContext

     @param context
     @return List<String>
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleContext([NotNull] RuleContextContext context)
    @Override
    public Object visitRuleContext(@NotNull FhirMapJavaParser.RuleContextContext context)
    {
      ArrayList<String> values = new ArrayList<String>();
      String[] retVals = new String[context.ruleContextElement().size()];
      if (context.ruleContextElement() != null){
        int count = context.ruleContextElement().size();
        for (ParseTree treeItem : context.ruleContextElement()){
          values.add((String) this.visit(treeItem));
        }
      }
      return values;    }

    /**
     Parse grammar rule ruleContextElement

     @param context
     @return String
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: public override Object VisitRuleContextElement([NotNull] RuleContextElementContext context)
    @Override
    public Object visitRuleContextElement(@NotNull FhirMapJavaParser.RuleContextElementContext context)
    {
        return this.visitChildren(context);
    }
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion
}

